/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx;

import java.io.File;
import java.net.MalformedURLException;
import java.security.Policy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.NotCompliantMBeanException;
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
import javax.slee.management.LibraryID;
import javax.slee.management.ManagementException;
import javax.slee.management.UnrecognizedDeployableUnitException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.du.DeployableUnit;
import org.mobicents.slee.container.component.du.DeployableUnitBuilder;
import org.mobicents.slee.container.component.du.DeployableUnitManagement;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.security.PermissionHolder;
import org.mobicents.slee.container.component.security.PolicyFile;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.deployment.jboss.SleeContainerDeployerImpl;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * 
 * TODO rework all the deployment mess, there should be a single internal deployer and this mbean should just delegate operations to it.
 * Implementation of the deployment MBean.
 * 
 * @author M. Ranganathan
 * @author DERUELLE Jean - bug fix throw UnrecognizedDeployableUnitException on
 *         method getDescriptor(DeplyableUnit) if no descriptor has been found
 * @author Eduardo Martins
 */
public class DeploymentMBeanImpl extends MobicentsServiceMBeanSupport implements
		DeploymentMBeanImplMBean {

	private File tempDUJarsDeploymentRoot;

	private final static Logger logger = Logger
			.getLogger(DeploymentMBeanImpl.class);	

	private final SleeContainerDeployerImpl deployer;
	
	public DeploymentMBeanImpl(SleeContainerDeployerImpl deployer) {
		super(deployer.getSleeContainer());
		this.tempDUJarsDeploymentRoot = createTempDUJarsDeploymentRoot();
		this.deployer = deployer;
		deployer.setDeploymentMBean(this);
	}
	
	/**
	 * 
	 * Sets the root directory that will be used for unpacking DU jars.
	 * 
	 * @TODO: make sure to remove this directory on undeploy
	 * 
	 */
	private File createTempDUJarsDeploymentRoot() {

		File tempDeploymentRootDir = new File(getSleeContainer().getDeployPath());
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
			final SleeContainer sleeContainer = getSleeContainer();
			final DeployableUnitBuilder deployableUnitBuilder = sleeContainer.getDeployableUnitManagement().getDeployableUnitBuilder();
			final SleeTransactionManager sleeTransactionManager = sleeContainer
					.getTransactionManager();
			final ComponentRepository componentRepositoryImpl = sleeContainer
					.getComponentRepository();
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

				Set<SleeComponent> componentsInstalled = new HashSet<SleeComponent>();
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
						componentsInstalled.add(component);
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
						componentsInstalled.add(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					for (ResourceAdaptorComponent component : deployableUnit
							.getResourceAdaptorComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getResourceManagement()
								.installResourceAdaptor(component);
						componentsInstalled.add(component);
						updateSecurityPermissions(component, false);
						logger.info("Installed " + component);
					}
					for (SbbComponent component : deployableUnit
							.getSbbComponents().values()) {
						currentThread.setContextClassLoader(component
								.getClassLoader());
						sleeContainer.getSbbManagement().installSbb(component);
						componentsInstalled.add(component);
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
						componentsInstalled.add(component);
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
									if(componentsInstalled.contains(component)) {
										sleeContainer.getResourceManagement().uninstallResourceAdaptorType(component);
									}
									componentRepositoryImpl.removeComponent(component.getResourceAdaptorTypeID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ProfileSpecificationComponent component : deployableUnit
										.getProfileSpecificationComponents()
										.values()) {
									if(componentsInstalled.contains(component)) {
										sleeContainer.getSleeProfileTableManager().uninstallProfileSpecification(component);
									}
									componentRepositoryImpl.removeComponent(component.getProfileSpecificationID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ResourceAdaptorComponent component : deployableUnit
										.getResourceAdaptorComponents().values()) {
									removeSecurityPermissions(component, false);
									if(componentsInstalled.contains(component)) {
										sleeContainer.getResourceManagement().uninstallResourceAdaptor(component);
									}
									componentRepositoryImpl.removeComponent(component.getResourceAdaptorID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (SbbComponent component : deployableUnit
										.getSbbComponents().values()) {
									removeSecurityPermissions(component, false);
									if(componentsInstalled.contains(component)) {
										sleeContainer.getSbbManagement().uninstallSbb(component);
									}
									componentRepositoryImpl.removeComponent(component.getSbbID());
									logger.info("Uninstalled " + component
											+ " due to tx rollback");
								}
								for (ServiceComponent component : deployableUnit
										.getServiceComponents().values()) {
									if(componentsInstalled.contains(component)) {
										sleeContainer.getServiceManagement().uninstallService(component);
									}
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
					"Failure encountered during deploy process.", e);
		} catch (Throwable e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(),e);
			}
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

		final SleeContainer sleeContainer = getSleeContainer();
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		final ServiceManagement serviceManagement = sleeContainer
				.getServiceManagement();
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();
		final DeployableUnitManagement deployableUnitManagement = sleeContainer
				.getDeployableUnitManagement();
		final ComponentRepository componentRepositoryImpl = sleeContainer
				.getComponentRepository();

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
								.getComponentRepository()
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

							// FIXME: For JBoss 7.2.0.Final:
							// we have a problem with org.hibernate.service.UnknownServiceException:
							// Unknown service requested [org.hibernate.event.service.spi.EventListenerRegistry]
							// see https://hibernate.atlassian.net/browse/HHH-8586
							for (ProfileSpecificationComponent component : deployableUnit
									.getProfileSpecificationComponents().values()) {
								currentThread.setContextClassLoader(component
										.getClassLoader());
								removeSecurityPermissions(component, false);
								sleeContainer.getSleeProfileTableManager()
										.closeEntityManagerFactory(component);
								logger.info("Finalized " + component);
							}

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
			if (getSleeContainer().getDeployableUnitManagement()
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
			return getSleeContainer().getDeployableUnitManagement()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			DeployableUnit du = getSleeContainer()
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

		final DeployableUnitManagement deployableUnitManagement = getSleeContainer().getDeployableUnitManagement();
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
	public String[] getEnvEntries(ComponentID componentID)
			throws NullPointerException, UnrecognizedComponentException,
			ManagementException {
		if (componentID == null)
			throw new NullPointerException("null component ID");
		if (logger.isDebugEnabled()) {
			logger.debug("getEnvEntries: componentID " + componentID);
		}
		try {
			ComponentRepository componentRepositoryImpl = getSleeContainer().getComponentRepository();
			SleeComponent component = null;
			if (componentID instanceof SbbID) {
				component = componentRepositoryImpl
						.getComponentByID((SbbID) componentID);
				List<EnvEntryDescriptor> envEntries = ((SbbComponent)component).getEnvEntries();
				String[] envEntriesString = new String[envEntries.size()];
				for (int i = 0; i < envEntries.size(); i++) {
					envEntriesString[i] = envEntries.get(i).toString();
				}
				return envEntriesString;
			}
			else
				return null;
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}
	 static public String[] toStringArray(EnvEntryDescriptor[] envEntries) {
	     if (envEntries == null)
	       return null;

	     String[] stringArray = new String[envEntries.length];

	     for (int i = 0; i < envEntries.length; i++)
	       stringArray[i] = envEntries[i].toString();

	     return stringArray;
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

		try {
			ComponentRepository componentRepositoryImpl = getSleeContainer().getComponentRepository();
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
			return getSleeContainer().getDeployableUnitManagement()
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
			return getSleeContainer().getComponentRepository()
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
			return getSleeContainer().getComponentRepository()
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
			ComponentRepository componentRepository = getSleeContainer().getComponentRepository();
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
		if (!(p instanceof PolicyFile)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not find Policy implmentation specific to mobicents. Found: " + p.getClass());
			}
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
		if (logger.isDebugEnabled()) {
				logger.debug("Could not find Policy implmentation specific to mobicents. Found: " + p.getClass());
			}
			return;
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
