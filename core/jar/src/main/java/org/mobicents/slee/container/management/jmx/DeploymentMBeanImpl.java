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

import org.jboss.logging.Logger;
import org.jboss.system.server.ServerConfigLocator;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.deployment.DeploymentManager;
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.DeployableUnitManagement;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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

	private static Logger logger;

	static {
		logger = Logger.getLogger(DeploymentMBeanImpl.class);
	}

	public DeploymentMBeanImpl() throws Exception {
		super(DeploymentMBean.class);
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
		
		File tempDeploymentRootDir = ServerConfigLocator.locate().getServerTempDeployDir();
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
	public synchronized DeployableUnitID install(String url)
			throws NullPointerException, MalformedURLException,
			AlreadyDeployedException, DeploymentException, ManagementException {

		logger.info("Installing DU with URL " + url);

		if (url == null) {
			throw new NullPointerException("null url");
		}
		URL deployUrl = new URL(url);

		try {
			final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			final DeployableUnitManagement deployableUnitManagement = sleeContainer
					.getDeployableUnitManagement();
			final SleeTransactionManager sleeTransactionManager = sleeContainer
					.getTransactionManager();

			synchronized (sleeContainer.getManagementMonitor()) {

				if (deployableUnitManagement.getDeployableUnitDescriptor(url) != null) {
					throw new AlreadyDeployedException(
							"there is already a DU deployed for url " + url);
				}

				boolean rollback = true;
				try {
					// start transaction
					sleeTransactionManager.begin();

					DeployableUnitID did = new DeploymentManager().deployUnit(
							deployUrl, this.tempDUJarsDeploymentRoot,
							this.classPath, sleeContainer);

					logger.info("Deployable unit with URL " + url
							+ " deployed as " + did);

					rollback = false;
					return did;
				} finally {
					try {
						if (rollback) {
							sleeTransactionManager.rollback();
						} else {
							sleeTransactionManager.commit();
						}
					} catch (Exception ex) {
						throw new ManagementException(
								"Exception while completing transaction", ex);
					}
				}
			}
		} catch (AlreadyDeployedException e) {
			throw e;
		} catch (DeploymentException e) {
			throw e;
		} catch (Exception e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#uninstall(javax.slee.management.DeployableUnitID)
	 */
	public void uninstall(DeployableUnitID deployableUnitID)
			throws NullPointerException, UnrecognizedDeployableUnitException,
			DependencyException, InvalidStateException, ManagementException {

		logger.info("Uninstalling DU with id " + deployableUnitID);

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		final ComponentManagement componentManagement = sleeContainer
				.getComponentManagement();
		final DeployableUnitManagement deployableUnitManagement = sleeContainer
				.getDeployableUnitManagement();
		final ServiceManagement serviceManagement = sleeContainer
				.getServiceManagement();
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();

		
		Thread currentThread = Thread.currentThread();
		ClassLoader currentClassLoader = null;
		
		// we sync on the container's monitor object
		synchronized (sleeContainer.getManagementMonitor()) {

			if (this.isInstalled(deployableUnitID)) {
				boolean rollback = true;
				try {
					// start transaction
					sleeTransactionManager.begin();
					// get du descriptor
					DeployableUnitDescriptorImpl deployableUnitDescriptor = deployableUnitManagement
							.getDeployableUnitDescriptor(deployableUnitID);
					
					DeployableUnitIDImpl deployableUnitIDImpl = deployableUnitDescriptor
							.getDeployableUnit();
					// Check if its safe to remove the deployable unit.
					if (deployableUnitManagement
							.hasReferringDU(deployableUnitDescriptor)) {
						throw new DependencyException(
								"Somebody is referencing a component of this DU"
										+ " -- cannot uninstall it!");
					}

					// check all services are deactivated
					serviceManagement
							.checkAllDUServicesAreDeactivated(deployableUnitIDImpl);
					
					// change class loader
					currentClassLoader = currentThread.getContextClassLoader();
					currentThread.setContextClassLoader(deployableUnitIDImpl.getDUDeployer().getClassLoader());
					
					// remove du components referring sets
					for (ComponentID cid : deployableUnitDescriptor
							.getComponents()) {
						componentManagement.removeComponentDependencies(cid);
					}

					resourceManagement.uninstallRA(deployableUnitIDImpl);
					resourceManagement.uninstallRAType(deployableUnitIDImpl);
					sleeContainer.getEventManagement().removeEventType(
							deployableUnitIDImpl);
					serviceManagement.uninstallServices(deployableUnitIDImpl);

					sleeContainer
							.getSleeProfileManager()
							.getProfileSpecificationManagement()
							.uninstallProfileSpecification(deployableUnitIDImpl);
					sleeContainer.getSbbManagement().uninstallSbbs(
							deployableUnitIDImpl);
					
					// remove du
					deployableUnitManagement
							.removeDeployableUnit(deployableUnitIDImpl);

					// Clean up all the class files.
					new DeploymentManager().undeployUnit(deployableUnitIDImpl);

					// restore classloader
					currentThread.setContextClassLoader(currentClassLoader);
					currentClassLoader = null;
					
					rollback = false;
					
					logger.info("Uninstalled DU with id " + deployableUnitID);
					
				} catch (InvalidStateException ex) {
					logger.error(ex);
					throw ex;
				} catch (DependencyException ex) {
					logger.error(ex);
					throw ex;
				} catch (Exception ex) {
					if (logger.isDebugEnabled())
						logger.debug(ex);
					throw new ManagementException(
							"Exception removing deployable Unit ", ex);
				} finally {
					try {
						if (currentClassLoader != null) {
							currentThread.setContextClassLoader(currentClassLoader);
						}
						if (rollback) {
							sleeTransactionManager.rollback();
						} else {
							sleeTransactionManager.commit();
						}
					} catch (Exception ex) {
						throw new ManagementException(
								"Exception while completing transaction", ex);
					}

				}
			} else {
				throw new UnrecognizedDeployableUnitException(
						"deployable unit " + deployableUnitID);
			}
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

		if (deploymentUrl == null) {
			throw new NullPointerException("null url");
		}

		DeployableUnitDescriptorImpl descriptor = SleeContainer
				.lookupFromJndi().getDeployableUnitManagement()
				.getDeployableUnitDescriptor(deploymentUrl);
		if (descriptor == null) {
			throw new UnrecognizedDeployableUnitException();
		}

		return descriptor.getDeployableUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getDeployableUnits()
	 */
	public DeployableUnitID[] getDeployableUnits() throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnits();
		} catch (Exception e) {
			throw new ManagementException("failed to get deployable units", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getSbbs()
	 */
	public SbbID[] getSbbs() throws ManagementException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getSbbs()");
			}
			return SleeContainer.lookupFromJndi().getSbbManagement()
					.getSbbIDs();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getEventTypes()
	 */
	public EventTypeID[] getEventTypes() throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getEventManagement()
					.getEventTypes();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getProfileSpecifications()
	 */
	public ProfileSpecificationID[] getProfileSpecifications()
			throws ManagementException {
		if (logger.isDebugEnabled()) {
			logger.debug("getProfileSpecifications");
		}
		try {
			return SleeContainer.lookupFromJndi().getSleeProfileManager()
					.getProfileSpecificationManagement()
					.getProfileSpecificationIDs();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getServices()
	 */
	public ServiceID[] getServices() throws ManagementException {
		if (logger.isDebugEnabled()) {
			logger.debug("getServices()");
		}
		try {
			return SleeContainer.lookupFromJndi().getServiceManagement()
					.getServiceIDs();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getResourceAdaptorTypes()
	 */
	public ResourceAdaptorTypeID[] getResourceAdaptorTypes()
			throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorTypeIDs();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getResourceAdaptors()
	 */
	public ResourceAdaptorID[] getResourceAdaptors() throws ManagementException {
		try {
			return SleeContainer.lookupFromJndi().getResourceManagement()
					.getResourceAdaptorIDs();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
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

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		ComponentDescriptor cd = sleeContainer.getComponentManagement()
				.getComponentDescriptor(componentId);
		if (cd == null)
			throw new UnrecognizedComponentException("unrecognized component "
					+ componentId);
		try {
			return SleeContainer.lookupFromJndi().getComponentManagement()
					.getReferringComponents(componentId);
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getDescriptor(javax.slee.management.DeployableUnitID)
	 */
	public DeployableUnitDescriptor getDescriptor(
			DeployableUnitID deployableUnitID) throws NullPointerException,
			UnrecognizedDeployableUnitException, ManagementException {

		DeployableUnitDescriptor dud = null;
		try {

			if (logger.isDebugEnabled()) {
				logger.debug("getDescriptor " + deployableUnitID);
			}
			if (deployableUnitID == null)
				throw new NullPointerException(
						"deployableUnitID should not be null");

			dud = SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnitDescriptor(deployableUnitID);
			if (dud == null)
				throw new UnrecognizedDeployableUnitException(
						"unrecognized deployable unit " + deployableUnitID);

		} catch (NullPointerException ex) {
			throw ex;
		} catch (UnrecognizedDeployableUnitException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
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
			return SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnitDescriptors();
		} catch (Exception ex) {
			throw new ManagementException("Error in tx manager ", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getDescriptor(javax.slee.ComponentID)
	 */
	public ComponentDescriptor getDescriptor(ComponentID componentID)
			throws NullPointerException, UnrecognizedComponentException,
			ManagementException {
		if (componentID == null)
			throw new NullPointerException("null component ID");

		if (logger.isDebugEnabled()) {
			logger.debug("getDescriptor: componentID " + componentID);
		}
		SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
		try {
			ComponentDescriptor cd = serviceContainer.getComponentManagement()
					.getComponentDescriptor(componentID);
			if (cd == null)
				throw new UnrecognizedComponentException(
						"unrecognized component " + componentID);
			else
				return cd;
		} catch (IllegalArgumentException ex) {
			throw new ManagementException(" Illegal Component Type "
					+ componentID, ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getDescriptors(javax.slee.ComponentID[])
	 */
	public ComponentDescriptor[] getDescriptors(ComponentID[] componentIds)
			throws NullPointerException, ManagementException {

		if (componentIds == null)
			throw new NullPointerException("null component ids");
		try {
			return SleeContainer.lookupFromJndi().getComponentManagement()
					.getDescriptors(componentIds);
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#isInstalled(javax.slee.management.DeployableUnitID)
	 */
	public boolean isInstalled(DeployableUnitID deployableUnitID)
			throws NullPointerException, ManagementException {
		if (deployableUnitID == null)
			throw new NullPointerException("null component ids");

		try {
			return SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnitDescriptor(deployableUnitID) != null;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#isInstalled(javax.slee.ComponentID)
	 */
	public boolean isInstalled(ComponentID componentId)
			throws NullPointerException, ManagementException {
		if (componentId == null)
			throw new NullPointerException(
					"deployableUnitID should not be null");

		SleeContainer serviceContainer = SleeContainer.lookupFromJndi();
		return serviceContainer.getComponentManagement().isInstalled(
				componentId);
	}

	public LibraryID[] getLibraries() throws ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public SbbID[] getSbbs(ServiceID arg0) throws NullPointerException,
			UnrecognizedServiceException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}
