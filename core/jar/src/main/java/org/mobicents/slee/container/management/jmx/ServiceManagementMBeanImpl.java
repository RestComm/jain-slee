/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Implementation of the ServiceManagementMBean
 * 
 * @author M. Ranganathan
 */
public class ServiceManagementMBeanImpl extends StandardMBean implements
        ServiceManagementMBean {

    private static Logger logger;

    static {
        logger = Logger.getLogger(ServiceManagementMBeanImpl.class);
    }

    public ServiceManagementMBeanImpl() throws NotCompliantMBeanException {
        super(ServiceManagementMBean.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID)
     */
    public ServiceState getState(ServiceID serviceID)
            throws NullPointerException, UnrecognizedServiceException,
            ManagementException {
        
        
        if ( logger.isDebugEnabled()) {
            logger.debug("Service.getState " + serviceID );
        }
        
        if ( serviceID == null ) throw new NullPointerException ("Null service ID!");
        SleeTransactionManager transactionManager = SleeContainer.getTransactionManager(); 
        boolean b = false;
        try {
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();

            Service service = null;

            b = transactionManager.requireTransaction();

            service = serviceContainer.getService(serviceID);

            if (service == null)
                throw new UnrecognizedServiceException(" unrecognized service "
                        + serviceID);
            if ( logger.isDebugEnabled()) {
                logger.debug("returning state " + service.getState());
            }
            return service.getState();

        } catch (Exception ex) {
            
            try {
                transactionManager.setRollbackOnly();
            } catch (SystemException e) {
                logger.error("Failed getState for serviceID " + serviceID);
            }
            throw new ManagementException(
                    "Unexpected system exception while getting state of service: " + serviceID, ex);
        } finally {
            if (b)
                try {
                    transactionManager.commit();
                } catch (SystemException e) {
                    logger.error("Failed getState for serviceID " + serviceID);
                    throw new ManagementException(
                            "Unexpected system exception while committing transaction after getState for serviceID: " + serviceID, e);
                }
        }
        

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.management.ServiceState)
     */
    public ServiceID[] getServices(ServiceState serviceState)
            throws NullPointerException, ManagementException {
        if (serviceState == null)
        	throw new NullPointerException("Passed a null state");
    	try {
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
            ServiceID[] retval = serviceContainer
                    .getServicesByState(serviceState);

            return retval;
        } catch (Exception ex) {
            throw new ManagementException("Error getting services by state!",ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
     */
    public void activate(ServiceID serviceID) throws NullPointerException,
            UnrecognizedServiceException, InvalidStateException,
            ManagementException {
        if (logger.isTraceEnabled())
            logger.trace("activate() " + serviceID);
        
    	if (serviceID == null)
			throw new NullPointerException("NullPointerException");
        try {
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
            serviceContainer.startService(serviceID);
        } catch (InvalidStateException ise) {
        	throw ise;
		} catch (Exception ex) {
            throw new ManagementException("system exception starting service",
                    ex);
        }
    }

    /*
    
    public void activate(ServiceID serviceID) throws NullPointerException,
           UnrecognizedServiceException, InvalidStateException,
           ManagementException {
       
   	if (logger.isDebugEnabled()) {
			logger.debug("Activating " + serviceID);
		}
       
		if (serviceID == null)
			throw new NullPointerException("null service id");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
		
		synchronized (sleeContainer) {
			
			
			boolean b = transactionManager.requireTransaction();
			boolean rb = true;
			
			try {
				
				Service service = sleeContainer.getService(serviceID);
				
				if (service == null) {
					throw new UnrecognizedServiceException("Unrecognized service "
							+ serviceID);
				}

				if (service.getState().equals(ServiceState.ACTIVE)) {
					throw new InvalidStateException("Service "+serviceID+" already active");
				}

				// If there was a deactivate before we have sbb entities pending,
				// remove those first
				RootSbbEntitiesRemovalTask task = RootSbbEntitiesRemovalTask.getTask(serviceID);
				if (task != null) {
					task.run();
					if (logger.isDebugEnabled()) {
						logger
								.debug("Found timer task running to remove remaining sbb entities. Executing now...");
					}
				}

				// notifying the resource adaptors about service activation
				for (sleeContainer.getResourceManagement().getResourceAdaptorEntities())
				ServiceComponent svcComponent = sleeContainer.getServiceComponent(serviceID);
				HashSet sbbIDs = svcComponent.getSbbComponents();
				HashSet raEntities = new HashSet();
				Iterator i = sbbIDs.iterator();
				while (i.hasNext()) {
					SbbDescriptor sbbdesc = sleeContainer.getSbbComponent((SbbID) i.next());
					if (sbbdesc != null) {
						String[] raLinks = sbbdesc.getResourceAdaptorEntityLinks();
						for (int c = 0; raLinks != null && c < raLinks.length; c++) {
							ResourceAdaptorEntity raEntity = getRAEntity(raLinks[c]);
							if (raEntity != null && !raEntities.contains(raEntity)) {
								raEntity.serviceActivated(serviceID.toString());
								raEntities.add(raEntity);
							}
						}
					}
				}

				// Already active just return.
				service.activate();
				getDeploymentCacheManager().getActiveServiceIDs().add(serviceID);
				svcComponent.lock();
				rb = false;
				logger.info("Activated " + serviceID);
			} catch (InvalidStateException ise) {
				throw ise;
			} catch (UnrecognizedServiceException use) {
				throw use;
			} catch (Exception ex) {
				throw new ManagementException("system exception starting service",
	                    ex);
			} finally {

				try {
					if (rb)
						transactionManager.setRollbackOnly();
					if (b)
						transactionManager.commit();

				} catch (Exception e) {
					logger.error("Failed: transaction commit", e);
				}
			}
		}
		
		
   	
   	
   	
       try {
           SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
           serviceContainer.startService(serviceID);
       } catch (InvalidStateException ise) {
       	throw ise;
		} catch (Exception ex) {
           throw new ManagementException("system exception starting service",
                   ex);
       }
   }
   
    */
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
     */
    public void activate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		if (serviceIDs.length == 0)
		{
			throw new InvalidArgumentException("InvalidArgumentException");
		}
			
		for (int i = 0; i < serviceIDs.length; i++)
		{
			if (serviceIDs[i] == null)
			{
				throw new InvalidArgumentException("InvalidArgumentException");
			}
		}
		for (int i = 0; i < serviceIDs.length - 1; i++)
			for (int j = i + 1; j < serviceIDs.length; j++)
				if (serviceIDs[i] == (serviceIDs[j]))
				{
					throw new InvalidArgumentException("InvalidArgumentException");
				}
		try {
			for (int i = 0; i < serviceIDs.length; i++) {
				activate(serviceIDs[i]);
			}
		} catch (InvalidStateException ise) {
			throw ise;
		} catch (Exception ex) {
			throw new ManagementException("system exception starting service",
					ex);
		}
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
     */
    public void deactivate(ServiceID serviceID) throws NullPointerException,
            UnrecognizedServiceException, InvalidStateException,
            ManagementException {

        
    	if (serviceID == null)
			throw new NullPointerException("NullPointerException");
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        try {
            serviceContainer.stopService(serviceID);
        } catch (InvalidStateException ise) {
        	throw ise;
		}catch (Exception ex) {
		    logger.error("Could not deactivate service", ex);
            throw new ManagementException(
                    "exception whild deactivating service ! ",ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID[])
     */
    public void deactivate(ServiceID[] arg0) throws NullPointerException,
            InvalidArgumentException, UnrecognizedServiceException,
            InvalidStateException, ManagementException {
       
		if (arg0.length == 0)
		{
			throw new InvalidArgumentException("InvalidArgumentException");
		}
			
		for (int i = 0; i < arg0.length; i++)
		{
			if (arg0[i] == null)
			{
				throw new InvalidArgumentException("InvalidArgumentException");
			}
		}
		for (int i = 0; i < arg0.length - 1; i++)
			for (int j = i + 1; j < arg0.length; j++)
				if (arg0[i] == (arg0[j]))
				{
					throw new InvalidArgumentException("InvalidArgumentException");
				}
		try {
			for (int i = 0; i < arg0.length; i++) {
				deactivate(arg0[i]);
			}
		} catch (InvalidStateException ise) {
			throw ise;
		} catch (Exception ex) {
			throw new ManagementException("system exception starting service",
					ex);
		}
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID,
     *      javax.slee.ServiceID)
     */
    public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
            throws NullPointerException, InvalidArgumentException,
            UnrecognizedServiceException, InvalidStateException,
            ManagementException {
        if (logger.isInfoEnabled()) 
            logger.debug("deactivateAndActivate (" + arg0 +" , "+ arg1 );
    	if (arg0 == arg1)
    		throw new InvalidArgumentException("Activating and deactivating the same service!");	
    	if ((arg0 == null) || (arg1 == null))
    		throw new InvalidArgumentException("The service(s) are null!");	
    	try {
        	deactivate(arg0);
            activate(arg1);
        } catch (InvalidStateException ise) {
        	throw ise;
		}catch (Exception ex) {
            throw new ManagementException(
                    "exception in deactivating/activating service ! ");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID[],
     *      javax.slee.ServiceID[])
     */
    public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
            throws NullPointerException, InvalidArgumentException,
            UnrecognizedServiceException, InvalidStateException,
            ManagementException {
        
    	if(arg0.length == 0 || arg1.length == 0)
    		throw new InvalidArgumentException("The service array is empty!");
    	for (int i = 0; i < arg0.length; i++)
			if (arg0[i] == null)	
				throw new InvalidArgumentException("InvalidArgumentException");

		for (int i = 0; i < arg1.length; i++)
			if (arg1[i] == null)
				throw new InvalidArgumentException("InvalidArgumentException");
		for (int i = 0; i < arg0.length - 1; i++)
			for (int j = i + 1; j < arg0.length; j++)
				if (arg0[i] == (arg0[j]))
					throw new InvalidArgumentException("InvalidArgumentException");
		for (int i = 0; i < arg1.length - 1; i++)
			for (int j = i + 1; j < arg1.length; j++)
				if (arg1[i] == (arg1[j]))
					throw new InvalidArgumentException("InvalidArgumentException");
		
		for (int i = 0; i < arg0.length; i++)
			for (int j = 0; j < arg1.length; j++)
				if (arg0[i] == (arg1[j]))
					throw new InvalidArgumentException(
							"InvalidArgumentException");
		try {
    		for (int i = 0; i < arg0.length; i++) {
        		deactivate(arg0[i]);
            }
    		for (int i = 0; i < arg1.length; i++) {
        		activate(arg1[i]);
            }
        }catch (InvalidStateException ise) {
        	throw ise;
		}
		catch (ManagementException me) {
			throw me;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
     */
    public ObjectName getServiceUsageMBean(ServiceID serviceID)
            throws NullPointerException, UnrecognizedServiceException,
            ManagementException {
        try {
            if (serviceID == null)
                throw new NullPointerException("Null service ID ");
            logger.debug("getServiceUsageMBean " + serviceID);
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();

            if ( ! serviceContainer.checkServiceExists(serviceID))
                    throw new UnrecognizedServiceException("bad service id "
                            + serviceID);

            ObjectName objName = new ObjectName("slee:ServiceUsageMBean="
                    + serviceID.toString());
            return objName;
        } catch (Exception ex) {
            throw new ManagementException(ex.getMessage(), ex);
        }
    }

}

