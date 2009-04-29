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
import java.security.Policy;
import java.util.HashSet;
import java.util.Set;

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
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.DeployableUnit;
import org.mobicents.slee.container.component.deployment.DeployableUnitBuilder;
import org.mobicents.slee.container.component.management.DeployableUnitManagement;
import org.mobicents.slee.container.component.security.PermissionHolder;
import org.mobicents.slee.container.component.security.PolicyFile;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Implementation of the deployment MBean.
 * 
 * @author M. Ranganathan
 * @author DERUELLE Jean - bug fix throw UnrecognizedDeployableUnitException on
 *         method getDescriptor(DeplyableUnit) if no descriptor has been found
 * @author Eduardo Martins
 */
public class DeploymentMBeanImpl extends StandardMBean implements
		DeploymentMBean {

	private File tempDUJarsDeploymentRoot;

	private final static Logger logger = Logger
			.getLogger(DeploymentMBeanImpl.class);

	/**
	 * builds DUs
	 */
	private final DeployableUnitBuilder deployableUnitBuilder = new DeployableUnitBuilder();

	public DeploymentMBeanImpl() throws Exception {
		super(DeploymentMBean.class);
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

		File tempDeploymentRootDir = ServerConfigLocator.locate()
				.getServerTempDeployDir();
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
	public DeployableUnitID install(String url) throws NullPointerException,
			MalformedURLException, AlreadyDeployedException,
			DeploymentException, ManagementException {		

		try {
			final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			final SleeTransactionManager sleeTransactionManager = sleeContainer
					.getTransactionManager();
			final ComponentRepositoryImpl componentRepositoryImpl = sleeContainer
					.getComponentRepositoryImpl();
			final DeployableUnitManagement deployableUnitManagement = sleeContainer
					.getDeployableUnitManagement();

			synchronized (sleeContainer.getManagementMonitor()) {

				DeployableUnitID deployableUnitID = new DeployableUnitID(url);

				logger.info("Installing " +deployableUnitID);
				
				if (deployableUnitManagement
						.getDeployableUnit(deployableUnitID) != null) {
					throw new AlreadyDeployedException(
							"there is already a DU deployed for url " + url);
				}

				DeployableUnit deployableUnit = null;

				Thread currentThread = Thread.currentThread();
				ClassLoader currentClassLoader = currentThread
						.getContextClassLoader();

				boolean rollback = true;
				try {
					// start transaction
					sleeTransactionManager.begin();
					// build du
					deployableUnit = deployableUnitBuilder.build(url,
							tempDUJarsDeploymentRoot, componentRepositoryImpl);
					// install each component built
					for (LibraryComponent component : deployableUnit
							.getLibraryComponents().values()) {
						componentRepositoryImpl.putComponent(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					for (EventTypeComponent component : deployableUnit
							.getEventTypeComponents().values()) {
						componentRepositoryImpl.putComponent(component);
						logger.info("Installed " + component);
					}
					for (ResourceAdaptorTypeComponent component : deployableUnit
							.getResourceAdaptorTypeComponents().values()) {
						componentRepositoryImpl.putComponent(component);
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getResourceManagement()
								.installResourceAdaptorType(component);
						logger.info("Installed " + component);
					}
					// before executing the logic to install an profile spec, ra
					// or sbb insert the components in the repo, a component can
					// require that another is already in repo
					for (ProfileSpecificationComponent component : deployableUnit
							.getProfileSpecificationComponents().values()) {
						componentRepositoryImpl.putComponent(component);
					}
					for (ResourceAdaptorComponent component : deployableUnit
							.getResourceAdaptorComponents().values()) {
						componentRepositoryImpl.putComponent(component);
					}
					for (SbbComponent component : deployableUnit
							.getSbbComponents().values()) {
						componentRepositoryImpl.putComponent(component);
					}
					// run the install logic to install an profile spec, ra or
					// sbb
					for (ProfileSpecificationComponent component : deployableUnit
							.getProfileSpecificationComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getSleeProfileTableManager()
								.installProfileSpecification(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					for (ResourceAdaptorComponent component : deployableUnit
							.getResourceAdaptorComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getResourceManagement()
								.installResourceAdaptor(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					for (SbbComponent component : deployableUnit
							.getSbbComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getSbbManagement().installSbb(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					// finally install the services
					currentThread.setContextClassLoader(currentClassLoader);
					for (ServiceComponent component : deployableUnit
							.getServiceComponents().values()) {
						componentRepositoryImpl.putComponent(component);
						sleeContainer.getServiceManagement().installService(
								component);
						logger.info("Installed " + component+". Root sbb is "+component.getRootSbbComponent());
					}
					
					deployableUnitManagement.addDeployableUnit(deployableUnit);
					logger.info("Installed " +deployableUnitID);
					updateSecurityPermissions(null, true);
					rollback = false;
					
					return deployableUnitID;
				} finally {
					currentThread.setContextClassLoader(currentClassLoader);
					try {
						if (rollback) {														
							if (deployableUnit != null) {
								// remove all components added to repo
								// put all components in the repo again
								for (LibraryComponent component : deployableUnit
										.getLibraryComponents().values()) {
									removeSecurityPermissions(component, false);
									componentRepositoryImpl.removeComponent(component.getLibraryID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (EventTypeComponent component : deployableUnit
										.getEventTypeComponents().values()) {
									componentRepositoryImpl.removeComponent(component.getEventTypeID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ResourceAdaptorTypeComponent component : deployableUnit
										.getResourceAdaptorTypeComponents()
										.values()) {
									removeSecurityPermissions(component, false);
									componentRepositoryImpl.removeComponent(component.getResourceAdaptorTypeID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ProfileSpecificationComponent component : deployableUnit
										.getProfileSpecificationComponents()
										.values()) {
									componentRepositoryImpl.removeComponent(component.getProfileSpecificationID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ResourceAdaptorComponent component : deployableUnit
										.getResourceAdaptorComponents().values()) {
									removeSecurityPermissions(component, false);
									componentRepositoryImpl.removeComponent(component.getResourceAdaptorID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (SbbComponent component : deployableUnit
										.getSbbComponents().values()) {
									removeSecurityPermissions(component, false);
									componentRepositoryImpl.removeComponent(component.getSbbID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ServiceComponent component : deployableUnit
										.getServiceComponents().values()) {
									componentRepositoryImpl.removeComponent(component.getServiceID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								removeSecurityPermissions(null, true);
								// undeploy the unit
								deployableUnit.undeploy();
							}
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
			// This will remove stack trace;
			// throw e;
			throw new DeploymentException(
					"Failur encountered during deploy process.", e);
		} catch (Throwable e) {
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

		logger.info("Uninstalling " +deployableUnitID);

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		final ServiceManagement serviceManagement = sleeContainer
				.getServiceManagement();
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();
		final DeployableUnitManagement deployableUnitManagement = sleeContainer
				.getDeployableUnitManagement();
		final ComponentRepositoryImpl componentRepositoryImpl = sleeContainer
				.getComponentRepositoryImpl();

		// we sync on the container's monitor object
		synchronized (sleeContainer.getManagementMonitor()) {

			if (this.isInstalled(deployableUnitID)) {

				Thread currentThread = Thread.currentThread();
				ClassLoader currentClassLoader = currentThread
						.getContextClassLoader();

				DeployableUnit deployableUnit = null;
				boolean rollback = true;
				try {
					// start transaction
					sleeTransactionManager.begin();
					// get du
					deployableUnit = deployableUnitManagement
							.getDeployableUnit(deployableUnitID);

					// Check if its safe to remove the deployable unit.
					for (SleeComponent sleeComponent : deployableUnit
							.getDeployableUnitComponents()) {
						for (SleeComponent referringComponent : sleeContainer
								.getComponentRepositoryImpl()
								.getReferringComponents(sleeComponent)) {
							if (!referringComponent.getDeployableUnit()
									.getDeployableUnitID().equals(
											deployableUnitID)) {
								throw new DependencyException("Component "
										+ referringComponent
										+ " refers DU component "
										+ sleeComponent);
							}
						}
					}

					for (ServiceComponent component : deployableUnit
							.getServiceComponents().values()) {
						serviceManagement.uninstallService(component);
						componentRepositoryImpl.removeComponent(component
								.getServiceID());
						logger.info("Uninstalled " + component);
					}

					for (SbbComponent component : deployableUnit
							.getSbbComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						removeSecurityPermissions(component, false);
						sleeContainer.getSbbManagement()
								.uninstallSbb(component);
						componentRepositoryImpl.removeComponent(component
								.getSbbID());
						logger.info("Uninstalled " + component);
					}

					for (ResourceAdaptorComponent component : deployableUnit
							.getResourceAdaptorComponents().values()) {
						removeSecurityPermissions(component, false);
						resourceManagement.uninstallResourceAdaptor(component);
						componentRepositoryImpl.removeComponent(component
								.getResourceAdaptorID());
						logger.info("Uninstalled " + component);
					}

					for (ProfileSpecificationComponent component : deployableUnit
							.getProfileSpecificationComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						removeSecurityPermissions(component, false);
						sleeContainer.getSleeProfileTableManager()
								.uninstallProfileSpecification(component);
						componentRepositoryImpl.removeComponent(component
								.getProfileSpecificationID());
						logger.info("Uninstalled " + component);
					}

					for (ResourceAdaptorTypeComponent component : deployableUnit
							.getResourceAdaptorTypeComponents().values()) {
						resourceManagement
								.uninstallResourceAdaptorType(component);
						componentRepositoryImpl.removeComponent(component
								.getResourceAdaptorTypeID());
						logger.info("Uninstalled " + component);
					}

					for (EventTypeID componentID : deployableUnit
							.getEventTypeComponents().keySet()) {
						componentRepositoryImpl.removeComponent(componentID);
						logger.info("Uninstalled " + componentID);
					}

					for (LibraryID componentID : deployableUnit
							.getLibraryComponents().keySet()) {
						removeSecurityPermissions(componentRepositoryImpl.getComponentByID(componentID), false);
						componentRepositoryImpl.removeComponent(componentID);
						logger.info("Uninstalled " + componentID);
					}
					removeSecurityPermissions(null,true);
					// remove du
					deployableUnitManagement
							.removeDeployableUnit(deployableUnitID);

					rollback = false;

					logger.info("Uninstalled " +deployableUnitID);

				} catch (InvalidStateException ex) {
					logger.error(ex.getMessage(), ex);
					throw ex;
				} catch (DependencyException ex) {
					logger.error(ex.getMessage(), ex);
					throw ex;
				} catch (Throwable ex) {
					if (logger.isDebugEnabled())
						logger.debug(ex.getMessage(),ex);
					throw new ManagementException(
							"Exception removing deployable Unit ", ex);
				} finally {

					currentThread.setContextClassLoader(currentClassLoader);

					try {
						if (rollback) {
							if (deployableUnit != null) {	
								// put all components in the repo again
								for (LibraryComponent component : deployableUnit
										.getLibraryComponents().values()) {
									if (componentRepositoryImpl.putComponent(component)) {
										updateSecurityPermissions(component, false);
										logger.info("Reinstalled " + component
												+ " due to tx rollback");
									}
								}
								for (EventTypeComponent component : deployableUnit
										.getEventTypeComponents().values()) {
									if (componentRepositoryImpl.putComponent(component)) {
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								for (ResourceAdaptorTypeComponent component : deployableUnit
										.getResourceAdaptorTypeComponents()
										.values()) {
									if (componentRepositoryImpl.putComponent(component)) {
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								for (ProfileSpecificationComponent component : deployableUnit
										.getProfileSpecificationComponents()
										.values()) {
									if (componentRepositoryImpl.putComponent(component)) {
										updateSecurityPermissions(component, false);
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								for (ResourceAdaptorComponent component : deployableUnit
										.getResourceAdaptorComponents().values()) {
									if (componentRepositoryImpl.putComponent(component)) {
										updateSecurityPermissions(component, false);
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								for (SbbComponent component : deployableUnit
										.getSbbComponents().values()) {
									if (componentRepositoryImpl.putComponent(component)) {
										updateSecurityPermissions(component, false);
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								for (ServiceComponent component : deployableUnit
										.getServiceComponents().values()) {
									if (componentRepositoryImpl.putComponent(component)) {
									logger.info("Reinstalled " + component
											+ " due to tx rollback");
									}
								}
								updateSecurityPermissions(null, true);
								deployableUnitManagement
								.addDeployableUnit(deployableUnit);
							}
							sleeTransactionManager.rollback();
						} else {
							sleeTransactionManager.commit();
							// Clean up all the class files.
							deployableUnit.undeploy();
						}
					} catch (Throwable ex) {
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

		DeployableUnitID deployableUnitID = new DeployableUnitID(deploymentUrl);

		boolean duExists = true;
		try {
			if (SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnit(deployableUnitID) == null) {
				duExists = false;
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
		if (duExists) {
			return deployableUnitID;
		} else {
			throw new UnrecognizedDeployableUnitException(deploymentUrl);
		}
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
		} catch (Throwable e) {
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
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getSbbIDs().toArray(new SbbID[0]);
		} catch (Throwable ex) {
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
			if (logger.isDebugEnabled()) {
				logger.debug("getEventTypes()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getEventComponentIDs().toArray(new EventTypeID[0]);
		} catch (Throwable ex) {
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
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getProfileSpecifications()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getProfileSpecificationIDs().toArray(
							new ProfileSpecificationID[0]);
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#getServices()
	 */
	public ServiceID[] getServices() throws ManagementException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getServices()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getServiceIDs().toArray(new ServiceID[0]);
		} catch (Throwable ex) {
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
			if (logger.isDebugEnabled()) {
				logger.debug("getResourceAdaptorTypes()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getResourceAdaptorTypeIDs().toArray(
							new ResourceAdaptorTypeID[0]);
		} catch (Throwable ex) {
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
			if (logger.isDebugEnabled()) {
				logger.debug("getResourceAdaptors()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getResourceAdaptorIDs().toArray(new ResourceAdaptorID[0]);
		} catch (Throwable ex) {
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

		try {
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getReferringComponents(componentId);
		} catch (NullPointerException ex) {
			throw ex;
		} catch (UnrecognizedComponentException ex) {
			throw ex;
		} catch (Throwable ex) {
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

		if (deployableUnitID == null)
			throw new NullPointerException(
					"deployableUnitID should not be null");

		DeployableUnitDescriptor dud = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getDescriptor " + deployableUnitID);
			}
			DeployableUnit du = SleeContainer.lookupFromJndi()
					.getDeployableUnitManagement().getDeployableUnit(
							deployableUnitID);
			if (du != null) {
				dud = du.getSpecsDeployableUnitDescriptor();
			}
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}

		if (dud == null) {
			throw new UnrecognizedDeployableUnitException(
					"unrecognized deployable unit " + deployableUnitID);
		} else {
			return dud;
		}
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

		final DeployableUnitManagement deployableUnitManagement = SleeContainer
				.lookupFromJndi().getDeployableUnitManagement();
		try {
			Set<DeployableUnitDescriptor> result = new HashSet<DeployableUnitDescriptor>();
			for (DeployableUnitID deployableUnitID : deployableUnitManagement
					.getDeployableUnits()) {
				DeployableUnit deployableUnit = deployableUnitManagement
						.getDeployableUnit(deployableUnitID);
				result.add(deployableUnit.getSpecsDeployableUnitDescriptor());
			}
			return result.toArray(new DeployableUnitDescriptor[0]);
		} catch (Throwable ex) {
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

		ComponentDescriptor componentDescriptor = null;
		try {
			ComponentRepositoryImpl componentRepositoryImpl = SleeContainer
					.lookupFromJndi().getComponentRepositoryImpl();
			SleeComponent component = null;
			if (componentID instanceof EventTypeID) {
				component = componentRepositoryImpl
						.getComponentByID((EventTypeID) componentID);
			} else if (componentID instanceof LibraryID) {
				component = componentRepositoryImpl
						.getComponentByID((LibraryID) componentID);
			} else if (componentID instanceof ProfileSpecificationID) {
				component = componentRepositoryImpl
						.getComponentByID((ProfileSpecificationID) componentID);
			} else if (componentID instanceof ResourceAdaptorID) {
				component = componentRepositoryImpl
						.getComponentByID((ResourceAdaptorID) componentID);
			} else if (componentID instanceof ResourceAdaptorTypeID) {
				component = componentRepositoryImpl
						.getComponentByID((ResourceAdaptorTypeID) componentID);
			} else if (componentID instanceof SbbID) {
				component = componentRepositoryImpl
						.getComponentByID((SbbID) componentID);
			} else if (componentID instanceof ServiceID) {
				component = componentRepositoryImpl
						.getComponentByID((ServiceID) componentID);
			}
			if (component != null)
				return component.getComponentDescriptor();
			else 
				return null;
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
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
			ComponentDescriptor[] descriptors = new ComponentDescriptor[componentIds.length];
			for (int i = 0; i < descriptors.length; i++) {
				descriptors[i] = getDescriptor(componentIds[i]);
			}
			return descriptors;
		} catch (ManagementException ex) {
			throw ex;
		} catch (Throwable ex) {
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
			throw new NullPointerException("null deployableUnitID");
		try {
			return SleeContainer.lookupFromJndi().getDeployableUnitManagement()
					.getDeployableUnit(deployableUnitID) != null;
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.DeploymentMBean#isInstalled(javax.slee.ComponentID)
	 */
	public boolean isInstalled(ComponentID componentID)
			throws NullPointerException, ManagementException {
		if (componentID == null)
			throw new NullPointerException("null componentID");
		try {
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.isInstalled(componentID);
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	public LibraryID[] getLibraries() throws ManagementException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getLibraries()");
			}
			return SleeContainer.lookupFromJndi().getComponentRepositoryImpl()
					.getLibraryIDs().toArray(new LibraryID[0]);
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	public SbbID[] getSbbs(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, ManagementException {

		if (serviceID == null) {
			throw new NullPointerException("null serviceID");
		}

		Set<SbbID> result = null;
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getSbbs(serviceID)");
			}
			ComponentRepository componentRepository = SleeContainer
					.lookupFromJndi().getComponentRepositoryImpl();
			ServiceComponent serviceComponent = componentRepository
					.getComponentByID(serviceID);
			if (serviceComponent != null) {
				result = serviceComponent.getSbbIDs(componentRepository);
			}
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}

		if (result != null) {
			return result.toArray(new SbbID[0]);
		} else {
			throw new UnrecognizedServiceException(serviceID.toString());
		}
	}

	private void updateSecurityPermissions(SleeComponent component, boolean refresh)
	{
		if(System.getSecurityManager()==null)
		{
			return;
		}
		Policy p = Policy.getPolicy();
		if(!(p instanceof PolicyFile))
		{
			logger.error("Could nto find Policy implmentation specific to mobicents. Found: "+p.getClass());
			return;
		}
		
		PolicyFile policyFile = (PolicyFile) p;
		if(component!=null)
		{
			
			for(PermissionHolder ph : component.getPermissions())
			{

				policyFile.addPermissionHolder(ph, false);
			}
		}
		

		if(refresh)
			policyFile.refresh();
	}
	
	private void removeSecurityPermissions(SleeComponent component, boolean refresh)
	{
		
		if(System.getSecurityManager()==null)
		{
			return;
		}
			
		Policy p = Policy.getPolicy();
		if(!(p instanceof PolicyFile))
		{
			logger.error("Could nto find Policy implmentation specific to mobicents. Found: "+p.getClass());
		}
		
		PolicyFile policyFile = (PolicyFile) p;
		if(component!=null)
		{
			for(PermissionHolder ph : component.getPermissions())
			{
				policyFile.removePermissionHolder(ph, false);
			}
		}
		
		if(refresh)
			policyFile.refresh();
	}
}
