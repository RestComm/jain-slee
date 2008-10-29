/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.StandardMBean;
import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.LibraryID;
import javax.slee.management.ManagementException;
import javax.slee.management.UnrecognizedDeployableUnitException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.jboss.system.server.ServerConfig;
import org.jboss.system.server.ServerConfigLocator;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.deployment.DeploymentManager;

/**
 * Implementation of the deployment MBean.
 * 
 * @author M. Ranganathan
 * @author DERUELLE Jean - bug fix throw UnrecognizedDeployableUnitException on
 *         method getDescriptor(DeplyableUnit) if no descriptor has been found
 */
public class DeploymentMBeanImpl extends StandardMBean implements
        DeploymentMBean {

    private File tempDUJarsDeploymentRoot;

    private File classPath;

    private ConcurrentHashMap deployedUnits;

    private static Logger logger;

    private String concurrentAccess = null;

    static {
        logger = Logger.getLogger(DeploymentMBeanImpl.class);
    }

    public DeploymentMBeanImpl() throws Exception {
        super(DeploymentMBean.class);
        this.deployedUnits = new ConcurrentHashMap();
        this.classPath = new File(SleeContainer.getDeployPath());

        this.tempDUJarsDeploymentRoot = createTempDUJarsDeploymentRoot();
    }

    /**
     * 
     * Sets the root directory that will be used for unpacking DU jars.
     * 
     * @TODO: make sure to remove this directory on undeploy
     *  
     */
    private File createTempDUJarsDeploymentRoot() {
        ServerConfig config = ServerConfigLocator.locate();
        File basedir = config.getServerTempDir();

        // ${jboss.server.home.dir}/tmp/deploy
        File tempDeploymentRootDir = new File(basedir, "deploy");

        if (!tempDeploymentRootDir.exists()) {
            boolean dirCreated = tempDeploymentRootDir.mkdirs();
            if (!dirCreated)
                throw new SLEEException(
                        "failed to create temp deployment dir: "
                                + tempDeploymentRootDir);
        }
        return tempDeploymentRootDir;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#install(java.lang.String)
     */
    public synchronized DeployableUnitID install(String url) throws NullPointerException,
            MalformedURLException, AlreadyDeployedException,
            DeploymentException, ManagementException {
    	
    	boolean isInstalled = false;
    	DeployableUnitID did = null;
    	
    	
    	logger.info("Installing DU with URL "+url);
    	
    	try {
			did = getDeployableUnit(url);
			// getDeployableUnit(String deploymentUrl) hasn't thrown any exception
			isInstalled = true; // Already deployed (redeployment)
			
    	} catch (UnrecognizedDeployableUnitException e) {		
			if(logger.isDebugEnabled()) {
				logger.debug("First time deploying " + url);
			}
    		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		
		if (isInstalled) {
			logger.warn(url + ": " + "Already deployed " + "(" + did + ")");
		}
		else {
			if (concurrentAccess != null) {
		            throw new RuntimeException(
		                    "CONCURRENT ACCESS TO DEPLOYMENT MBEAN! Ongoing call to "
		                            + concurrentAccess);
			    }
		        concurrentAccess = "install(" + url + ")";
		        //DeployableUnitID did = null;
		        try {
		            DeploymentManager deploymentManager = new DeploymentManager();
		            URL deployUrl = new URL(url);
		            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
		            
		            did = deploymentManager.deployUnit(deployUrl, 
		            		this.tempDUJarsDeploymentRoot, this.classPath,
		            		serviceContainer);
		            // Put it in our table for later lookup.
		            this.deployedUnits.put(url, did);
		        	
		        } finally {
		            concurrentAccess = null;
		        }
		        
		        logger.info("Deployable unit with URL "+url+" deployed as " + did);
		}
		return did;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#uninstall(javax.slee.management.DeployableUnitID)
     */
    public synchronized void uninstall(DeployableUnitID deployableUnitID)
            throws NullPointerException, UnrecognizedDeployableUnitException,
            DependencyException, InvalidStateException, ManagementException {
        
    	logger.info("Uninstalling DU with id "+deployableUnitID);
    	
    	if (concurrentAccess != null) {
            throw new RuntimeException(
                    "CONCURRENT ACCESS TO DEPLOYMENT MBEAN! Ongoing call to "
                            + concurrentAccess);
        }
        concurrentAccess = "uninstall(" + deployableUnitID + ")";
        try {

            if (this.isInstalled(deployableUnitID)) {
                try {
                    SleeContainer serviceContainer = SleeContainer
                            .lookupFromJndi();
                    serviceContainer.removeDeployableUnit(deployableUnitID);
                    Iterator it = this.deployedUnits.values().iterator();
                    while (it.hasNext()) {
                        // clean up the url table.
                        if (it.next().equals(deployableUnitID))
                            it.remove();
                    }
                    logger.info("Uninstalled DU with id "+deployableUnitID);
                } catch (InvalidStateException ex) {
                	logger.error(ex);
                    throw ex;
                } catch (DependencyException ex) {
                	logger.error(ex);
                    throw ex;
                } catch (Exception ex) {
                  if(logger.isDebugEnabled())
                    logger.debug(ex);
                    throw new ManagementException(
                            "Exception removing deployable Unit ", ex);
                }
            } else
                throw new UnrecognizedDeployableUnitException(
                        "deployable unit " + deployableUnitID);
        } finally {
            concurrentAccess = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDeployableUnit(java.lang.String)
     */
    public DeployableUnitID getDeployableUnit(String deploymentUrl)
            throws NullPointerException, UnrecognizedDeployableUnitException,
            ManagementException {
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getDeployableUnitIDFromUrl(deploymentUrl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDeployableUnits()
     */
    public DeployableUnitID[] getDeployableUnits() throws ManagementException {
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getDeployableUnits();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getSbbs()
     */
    public SbbID[] getSbbs() throws ManagementException {
        try {
            if(logger.isDebugEnabled()) {
            	logger.debug("getSbbs()");
            }
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
            return serviceContainer.getSbbIDs();
        } catch (Exception ex) {
            throw new ManagementException(ex.getMessage(),ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getEventTypes()
     */
    public  EventTypeID[] getEventTypes() throws ManagementException {
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getEventTypes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getProfileSpecifications()
     */
    public ProfileSpecificationID[] getProfileSpecifications()
            throws ManagementException {
    	if(logger.isDebugEnabled()) {
    		logger.debug("getProfileSpecifications");
    	}
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getProfileSpecificationIDs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getServices()
     */
    public ServiceID[] getServices() throws ManagementException {
    	if(logger.isDebugEnabled()) {
    		logger.debug("getServices()");
    	}
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getServiceIDs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getResourceAdaptorTypes()
     */
    public ResourceAdaptorTypeID[] getResourceAdaptorTypes()
            throws ManagementException {
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getResourceManagement().getResourceAdaptorTypeIDs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getResourceAdaptors()
     */
    public  ResourceAdaptorID[] getResourceAdaptors() throws ManagementException {
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getResourceManagement().getResourceAdaptorIDs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getReferringComponents(javax.slee.ComponentID)
     */
    public ComponentID[] getReferringComponents(ComponentID componentId)
            throws NullPointerException, UnrecognizedComponentException,
            ManagementException {
        if (componentId == null)
            throw new NullPointerException(
                    "ComponentID should not be null. See SLEE 1.0 TCK test 3776.");
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.getReferringComponents(componentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDescriptor(javax.slee.management.DeployableUnitID)
     */
    public DeployableUnitDescriptor getDescriptor(
            DeployableUnitID deployableUnitID) throws NullPointerException,
            UnrecognizedDeployableUnitException, ManagementException {
        /*if (concurrentAccess != null) {
            throw new RuntimeException(
                    "CONCURRENT ACCESS TO DEPLOYMENT MBEAN! Ongoing call to "
                            + concurrentAccess);
        }
        concurrentAccess = "getDescriptor(" + deployableUnitID + ")";*/
        DeployableUnitDescriptor dud = null;
        try {

        	if(logger.isDebugEnabled()) {
        		logger.debug("getDescriptor " + deployableUnitID);
        	}
            if (deployableUnitID == null)
                throw new NullPointerException(
                        "deployableUnitID should not be null");
            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
            dud = serviceContainer
                    .getDeployableUnitDescriptor(deployableUnitID);
            if (dud == null)
                throw new UnrecognizedDeployableUnitException(
                        "unrecognized deployable unit " + deployableUnitID);
            /*
             * ComponentID[] components = dud.getComponents(); for ( int i = 0;
             * i < components.length; i++) { log.info("component " +
             * components[i]); }
             */
        } finally {
            concurrentAccess = null;
        }
        return dud;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDescriptors(javax.slee.management.DeployableUnitID[])
     */
    public DeployableUnitDescriptor[] getDescriptors(DeployableUnitID[] duIds)
            throws NullPointerException, ManagementException {
        if (duIds == null)
            throw new NullPointerException("Null arg!");
        try {

            SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
            return serviceContainer.getDeployableUnitDescriptors();
        } catch (SystemException ex) {
            throw new ManagementException("Error in tx manager ",ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDescriptor(javax.slee.ComponentID)
     */
    public   ComponentDescriptor getDescriptor(ComponentID componentID)
            throws NullPointerException, UnrecognizedComponentException,
            ManagementException {
        if (componentID == null)
            throw new NullPointerException("null component ID");

        if(logger.isDebugEnabled()) {
        	logger.debug("getDescriptor: componentID " + componentID);
        }
        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        try {
            ComponentDescriptor cd = serviceContainer
                    .getComponentDescriptor(componentID);
            if (cd == null)
                throw new UnrecognizedComponentException(
                        "unrecognized component " + componentID);
            else
                return cd;
        } catch (IllegalArgumentException ex) {
            throw new ManagementException(" Illegal Component Type "
                    + componentID,ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#getDescriptors(javax.slee.ComponentID[])
     */
    public  ComponentDescriptor[] getDescriptors(ComponentID[] componentIds)
            throws NullPointerException, ManagementException {
        if (componentIds == null)
            throw new NullPointerException("null component ids");
        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        return sleeContainer.getDescriptors(componentIds);
        
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#isInstalled(javax.slee.management.DeployableUnitID)
     */
    public  boolean isInstalled(DeployableUnitID deployableUnitID)
            throws NullPointerException, ManagementException {
        if (deployableUnitID == null)
            throw new NullPointerException("null component ids");

        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.isInstalled(deployableUnitID);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.DeploymentMBean#isInstalled(javax.slee.ComponentID)
     */
    public  boolean isInstalled(ComponentID componentId)
            throws NullPointerException, ManagementException {
        if (componentId == null)
            throw new NullPointerException(
                    "deployableUnitID should not be null");

        SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
        return serviceContainer.isInstalled(componentId);
    }

	public LibraryID[] getLibraries() throws ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public SbbID[] getSbbs(ServiceID arg0) throws NullPointerException, UnrecognizedServiceException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}

