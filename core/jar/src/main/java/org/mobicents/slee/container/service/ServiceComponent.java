/*
 * ServiceComponent.java
 * 
 * Created on Jun 7, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.service;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedSbbException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.ManagementException;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.ServiceState;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployedComponent;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.deployment.ConcreteUsageParameterMBeanInterfaceGenerator;
import org.mobicents.slee.container.management.jmx.SbbUsageMBeanImpl;


/**
 * This represents the service component - the static part of a service
 * including the descriptor and service state. This resides in the deployment
 * cache. The state in this object is persisted to disk and must survive SLEE
 * restarts. This structure includes the UsageMBeans and pointer to the Service
 * Descriptor.
 * 
 * @see Service
 * 
 *  
 */
public class ServiceComponent  implements DeployedComponent, Serializable {
   
    
    private ServiceIDImpl serviceID;
  
    //private ServiceState serviceState;
    private static Logger logger = Logger.getLogger(ServiceComponent.class);
    private DeployableUnitID deployableUnitID;   
    private ObjectName usageMBean;
    private boolean pendingRemove;
    private transient ServiceDescriptorImpl serviceDescriptor;
    
    

    // A set of usage parameter names. Index into the hash map is
    // the sbbid.
   
    private HashMap usageParameterNames;

    // A set of usage parameter object names.
    
    private HashMap usageParameterObjectNames;
    
    private HashSet sbbComponents;

    private boolean isLocked;

    public ServiceComponent(ServiceDescriptorImpl serviceDescriptor) throws Exception {
       
        this.serviceID = (ServiceIDImpl) serviceDescriptor.getID();
       
        //this.serviceState = ServiceState.INACTIVE;
        
        this.usageParameterNames = new HashMap();
        this.usageParameterObjectNames = new HashMap();
        this.deployableUnitID = serviceDescriptor.getDeployableUnit();
        this.serviceDescriptor = serviceDescriptor;
        this.sbbComponents = new HashSet();
        SbbID sbbid = this.serviceDescriptor.getRootSbb();
        this.enumerateSbbs(sbbid);

    }
    
    private void enumerateSbbs(SbbID sbbId) throws Exception {
    	
    	
		this.sbbComponents.add(sbbId);
		SbbDescriptor sbbDesc = SleeContainer.lookupFromJndi().getSbbComponent(
				sbbId);
		if (sbbDesc == null) {
			throw new Exception("Could not find SBB Component: " + sbbId);
		}
		SbbID[] sbbIds = sbbDesc.getSbbs();
		if (sbbIds != null) {
			for (int i = 0; i < sbbIds.length; i++) {
				SbbID nextSbbID = sbbIds[i];
				if (!sbbComponents.contains(nextSbbID)) {
					enumerateSbbs(sbbIds[i]);
				} else if (logger.isDebugEnabled()) {
					logger.debug("Skipping recursive call to avoid infinite recursion SbbID : "
									+ nextSbbID + " in method enumerateSbbs");
				}

			}
		}
	}	
    
    
    /**
     * Get the names of all usage parameters.
     * 
     * @return
     */
    public HashSet getAllUsageParameterNames() {
        HashSet retval = new HashSet();
        for (Iterator it = this.usageParameterNames.values().iterator(); it
                .hasNext();) {
            HashSet names = (HashSet) it.next();
            retval.addAll(names);
        }
        return retval;
    }

    
    

    
    
    protected void registerSbbUsageParameter(ServiceID serviceId, SbbID sbbId,
            ObjectName sbbUsageParameter) {
        String key = Service.getUsageParametersPathName(serviceId,sbbId);
        if ( logger.isDebugEnabled()) {
            logger.debug("registerSbbUsageParameter: serviceId = " + serviceId + " sbbId = " + sbbId + " key = " + key);
        }
        this.usageParameterObjectNames.put( key, sbbUsageParameter);
        
    }
    private void registerSbbUsageParameter(ServiceID serviceId, SbbID sbbId, String name,
            ObjectName sbbUsageParameter) {
        if ( logger.isDebugEnabled() ) {
            logger.debug("registerSbbUsageParameter: serviceId = " + serviceId +
                    " sbbId = "+ sbbId + 
                    " name = " + name + 
                    " sbbUsageParameter = " + sbbUsageParameter);
        }
        this.usageParameterObjectNames.put( Service.getUsageParametersPathName(serviceId,sbbId,name), sbbUsageParameter);
    }
    
    public ObjectName getUsageParameterObjectName( SbbID sbbId) {
        String key = Service.getUsageParametersPathName(serviceID,sbbId);
        logger.debug("getUsageParameterObjectName:  " + sbbId + " key = " + key);
        return (ObjectName) this.usageParameterObjectNames.get(key);
    }
    
    public ObjectName getUsageParameterObjectName( SbbID sbbId, String name)
    throws UnrecognizedUsageParameterSetNameException, InvalidArgumentException {
        
        String key = Service.getUsageParametersPathName(serviceID,sbbId,name);
       
        if ( ! this.getAllUsageParameterNames().contains(name)) {
            //logger.debug("usageParamNames = " + usageParameterNames);
            throw new UnrecognizedUsageParameterSetNameException
            ("Illegal arg - param name not found for this service " + name);
        }
        
        if (this.usageParameterNames.get(sbbId.toString()) == null 	) {
            throw new InvalidArgumentException ("no usage parameter found for this sbb");
        }
        
        logger.debug("getUsageParameterObjectName:  " + sbbId 
                + " name = " + name + " key = " + key);
         
       return (ObjectName) this.usageParameterObjectNames.get(key);
    }
    /**
     * Install a usage parameter set.
     * 
     * @param service
     * @param sbbId
     * @param name
     * @throws Exception
     */
    public void installUsageParameter(SbbID sbbId, String name)
            throws Exception {
        boolean b = SleeContainer.getTransactionManager().requireTransaction();

        if ( logger.isDebugEnabled() ) {
            logger.debug("installUsageParameter: sbbId = " + sbbId + " name = " + name);
        }
        try {
            if (sbbId == null || name == null)
                throw new NullPointerException(" Null arg! ");
            ServiceID serviceID = this.getServiceID();

            MobicentsSbbDescriptor sbbDescriptor = (MobicentsSbbDescriptor) SleeContainer
                    .lookupFromJndi().getSbbComponent(sbbId);
            
            if (sbbDescriptor == null)
                throw new UnrecognizedSbbException("Unrecognized Sbb");

           

            if (this.usageParameterNames.get(sbbId.toString()) != null
                    && ((HashSet) this.usageParameterNames.get(sbbId.toString()))
                            .contains(name))
                throw new UsageParameterSetNameAlreadyExistsException(
                        "Usage Parameter already exists " + name);

            // Create the actual usage parameter instance.
            Class usageParameterClass = sbbDescriptor.getUsageParameterClass();
            if (usageParameterClass == null)
                throw new RuntimeException("Usage parameter class not found !");
            Constructor cons = usageParameterClass.getConstructor(new Class[] {
                    ServiceIDImpl.class, SbbIDImpl.class });
            Object usageParm = cons
                    .newInstance(new Object[] { serviceID, sbbId });
            String pathName = Service.getUsageParametersPathName(this.getServiceID(),
                    (SbbIDImpl) sbbDescriptor.getID(), name);

            // The service instance houses the actual usage parameter.
            try {
                
                Service.addUsageParameter((ServiceID)this.serviceID,pathName,  usageParm);
            } catch ( Exception ex ) {
                throw new ManagementException ("Service not found " + serviceID);
            }
            
            HashSet hset = (HashSet) this.usageParameterNames.get(sbbId.toString());
            if (hset == null) {
                hset = new HashSet();
                this.usageParameterNames.put(sbbId.toString(), hset);
            }
            hset.add(name);
            
            // TODO: The following code creating the SbbUsageMBean instance is
            // the same
            // as in installDefaultUsageParameters. Should be extracted.
            String usageParameterMBeanClassName = sbbDescriptor
                    .getUsageParametersInterface()
                    + "MBeanImpl";
            Class[] args = {  ServiceID.class, SbbID.class,
                    String.class, String.class, usageParameterClass };
            Class usageParameterMBeanClass = sbbDescriptor.getClassLoader()
                    .loadClass(usageParameterMBeanClassName);
            Constructor constructor = usageParameterMBeanClass
                    .getConstructor(args);
            Object[] objs = { this.serviceID, sbbId,
                    name,
                    sbbDescriptor.getUsageParametersInterface() + "MBean",
                    usageParm };
            SbbUsageMBeanImpl usageMbean = (SbbUsageMBeanImpl) constructor
                    .newInstance(objs);
            String mbeanName = "slee:SbbUsageMBean=" + pathName;
            ObjectName oname = new ObjectName(mbeanName);
            SleeContainer.lookupFromJndi().getMBeanServer().registerMBean(
                    usageMbean, oname);
           
            registerSbbUsageParameter(this.serviceID, sbbId, name, oname);

            InstalledUsageParameterSet usageParam = (InstalledUsageParameterSet) usageParm;
            usageParam.setName(name);
            usageParam.setSbbUsageMBean(usageMbean);
            usageMbean.setUsageParameter(usageParam);
            
        } catch (Exception e) {
            String s = "Unexpected exception !";
            logger.error(s, e);
            try {
                SleeContainer.getTransactionManager().setRollbackOnly();
            } catch (SystemException ex) {
                s = "Tx manager Failure!";
            }
            throw e;
        } finally {
            try {
                if (b)
                    SleeContainer.getTransactionManager().commit();
            } catch (Exception ex) {
                throw new RuntimeException("Unexpected error in tx manager", ex);
            }
        }
    }

    /**
     * Installs the default usage parameter set.
     * 
     * @param descriptor --
     *            the sbb descriptor of the root sbb of the service.
     * 
     * @throws Exception
     */
    public void installDefaultUsageParameters(MobicentsSbbDescriptor descriptor, Set sbbsChecked)
            throws Exception {
    	
        logger.debug("Installing default usage parameter for "
                + descriptor.getID());
        ClassLoader currentClassLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
                descriptor.getClassLoader());
        try {
            if (descriptor.getUsageParametersInterface() != null) {
                Class usageParameterClass = descriptor.getUsageParameterClass();

                Constructor cons = usageParameterClass
                        .getConstructor(new Class[] { ServiceIDImpl.class,
                                SbbIDImpl.class });
                Object usageParm = cons.newInstance(new Object[] {
                        this.getServiceID(), descriptor.getID() });
                InstalledUsageParameterSet usageParam = (InstalledUsageParameterSet) usageParm;
                String pathName = Service.getUsageParametersPathName(this.getServiceID(),(SbbID) descriptor
                        .getID());

                if ( logger.isDebugEnabled()) {
                    logger.debug(" Putting usage parameter into table " + pathName);
                }

               
                Service.addUsageParameter(serviceID, pathName, usageParam);

                // Creating the custom MBean Interface
                ConcreteUsageParameterMBeanInterfaceGenerator mbeanInterfaceGenerator = new ConcreteUsageParameterMBeanInterfaceGenerator(
                        descriptor);
                mbeanInterfaceGenerator
                        .generateConcreteUsageParameterMBeanInterface();
                String usageParameterMBeanClassName = descriptor
                        .getUsageParametersInterface()
                        + "MBeanImpl";
                Class usageParameterMBeanClass = Thread.currentThread()
                        .getContextClassLoader().loadClass(
                                usageParameterMBeanClassName);
                Class[] args = {  ServiceID.class,
                        SbbID.class, String.class, String.class,
                        usageParameterClass };
                Constructor constructor = usageParameterMBeanClass
                        .getConstructor(args);

                Object[] objs = { this.serviceID,
                        (SbbID) descriptor.getID(), null,
                        descriptor.getUsageParametersInterface() + "MBean",
                        usageParam };
                Object usageMbean = constructor.newInstance(objs);
                usageParam.setSbbUsageMBean((SbbUsageMBeanImpl) usageMbean);
                ((SbbUsageMBeanImpl) usageMbean).setUsageParameter(usageParam);

                String mbeanName = "slee:SbbUsageMBean=" + pathName;
                ObjectName oname = new ObjectName(mbeanName);
                SleeContainer.lookupFromJndi().getMBeanServer().registerMBean(
                        usageMbean, oname);

               
                registerSbbUsageParameter(this.serviceID, (SbbID) descriptor
                        .getID(), oname);

                
            }
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
        SbbID[] sbbs = descriptor.getSbbs();
        if (sbbs == null)
            return;        
        for (int i = 0; i < sbbs.length; i++) {        	
			if (!sbbsChecked.contains(sbbs[i])) {
				// this sbb was not processed, add it to the set
				sbbsChecked.add(sbbs[i]);
				// get its descriptor
				MobicentsSbbDescriptor desc = (MobicentsSbbDescriptor) SleeContainer
						.lookupFromJndi().getSbbComponent(sbbs[i]);
				// recursive invocation
				this.installDefaultUsageParameters(desc,sbbsChecked);
			} else if ( logger.isDebugEnabled() ) {
				logger.debug("Skipping recursive step for SbbID : " + sbbs[i]
						+ " to avoid infinite recursion");
			}
        }

    }
   

    /**
     * Remove the usage parameter object and unregister the mbean for the usage 
     * parameter object.
     * 
     * @param sbbId
     * @param name
     * @throws ManagementException
     * @throws UnrecognizedUsageParameterSetNameException
     */
    public void removeUsageParameter(SbbID sbbId, String name)
            throws ManagementException,
            UnrecognizedUsageParameterSetNameException, UnrecognizedSbbException , 
            InvalidArgumentException  {

        
        if ( ! this.sbbComponents.contains(sbbId))
                throw new UnrecognizedSbbException ("SBB does not belong to this service " + sbbId);
        
        HashSet names = (HashSet) this.usageParameterNames
                .get(sbbId.toString());
       
        
        if ( names == null )
            throw new InvalidArgumentException ("UsageParameter name " + name + " not found ");
        
        if ( !names.contains(name)) {
            throw new UnrecognizedUsageParameterSetNameException(
                    "usage parameter name not found " + name);
        }
        names.remove(name);
        String pathName = Service.getUsageParametersPathName(this.getServiceID(),
                sbbId, name);
       
        try {
        
          Service.removeUsageParameter(serviceID, pathName);
        } catch ( Exception ex ) {
            throw new ManagementException ("Unexpected exception!", ex);
        }
        String mbeanName = "slee:SbbUsageMBean=" + pathName;
        ObjectName oname = null;
        try {
            oname = new ObjectName(mbeanName);
           
        } catch (MalformedObjectNameException e) {
            logger.fatal(" malformed url! " + mbeanName, e);
            throw new ManagementException("error unregistering mbean ", e);
        }
        // Remove this from our set of usage parameer object names.
        this.usageParameterObjectNames.remove(pathName);
        
        try {
            SleeContainer.lookupFromJndi().getMBeanServer().unregisterMBean(
                    oname);
        } catch (InstanceNotFoundException e1) {
            throw new ManagementException("Instance not found ! " + oname);
        } catch (MBeanRegistrationException e1) {
            logger.fatal("unexpected exception ", e1);
            throw new ManagementException("Instance not registered! " + oname);
        }
    }
    
    /**
     * Return the set of named usage parameter names as an array.
     * 
     * @param sbbId --
     *            sbb id for which to get the usage parameter set.
     * @return
     */
    public String[] getNamedUsageParameterSets(SbbID sbbId)  throws InvalidArgumentException {
       
        HashSet paramSet = (HashSet) this.usageParameterNames.get(sbbId.toString());
        String[] retval = null;
        if (paramSet != null) {
           
            retval = new String[paramSet.size()];
            paramSet.toArray(retval);
            return retval;
        } else
            throw new InvalidArgumentException ( "No named usage parameters for this sbb " + sbbId);
        
        

    }
    /**
     * Returns an iterator with all the usage parameter names.
     * 
     * @return
     */
    public Iterator getSbbUsageMBeans() {

        return this.usageParameterObjectNames.values().iterator();
    }

    

    
    /**
     * Return the deployable unit id for this service
     * 
     * @return
     */
    public DeployableUnitID getDeployableUnit() {

        return this.deployableUnitID;
    }

    public void setDeployableUnit(DeployableUnitID deployableUnitID) {
        this.deployableUnitID = deployableUnitID;
    }

    

    
    /**
     * Get the service id for this service component.
     * 
     * @return serviceID
     */
    public ServiceID getServiceID() {

        return this.serviceID;
    }

    /**
     * @return
     */
    public ServiceDescriptorImpl getServiceDescriptor() {
        
        return this.serviceDescriptor;
    }

    /**
     * Get the root sbb component for the service
     * 
     * @return the root sbb component for the service.
     */
    public MobicentsSbbDescriptor getRootSbbComponent() {
        return (MobicentsSbbDescriptor) SleeContainer.lookupFromJndi()
                .getSbbComponent(serviceDescriptor.getRootSbb());
    }

    public void setUsageMBeanName(ObjectName usageMBean) {
        this.usageMBean = usageMBean;

    }
    
   

    public ObjectName getUsageMBean() {
        return this.usageMBean;
    }

    
    /**
     * Mark for garbage collection.
     *  
     */
    public void markForRemove() {
        this.pendingRemove = true;
    }

    /**
     * Return true if marked for GC.
     */
    public boolean isMarkedForRemove() {
        return this.pendingRemove;
    }

    
   

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#checkDeployment()
     */
    public void checkDeployment() throws DeploymentException {
        
        
    }

    /**
     * @param sbbId
     * @return
     */
    public boolean isComponent(SbbID sbbId) {
        // TODO Auto-generated method stub
        return this.sbbComponents.contains(sbbId);
    }

    /**
     * @return
     */
    public void lock() {
       
        this.isLocked = true;
    }
    
    public boolean isLocked() {
        return this.isLocked;
    }

    public void unlock() {
        this.isLocked = false;
    }
    
    

    

    /**
     * @return Returns the sbbComponents.
     */
    public HashSet getSbbComponents() {
        return sbbComponents;
    }
}

