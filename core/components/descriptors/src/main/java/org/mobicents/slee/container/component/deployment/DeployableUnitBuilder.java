/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component.deployment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DependencyException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.log4j.Logger;
//import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.classloading.ComponentClassLoader;
import org.mobicents.slee.container.component.deployment.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileTableInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbActivityContextInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbLocalInterface;

public class DeployableUnitBuilder {

	private static final Logger logger = Logger
			.getLogger(DeployableUnitBuilder.class);

	private static final DeployableUnitJarComponentBuilder duComponentBuilder = new DeployableUnitJarComponentBuilder();
	private static final DeployableUnitServiceComponentBuilder duServiceComponentBuilder = new DeployableUnitServiceComponentBuilder();

	/**
	 * Installs a JAIN SLEE DU.
	 * 
	 * @param sourceUrl
	 *            the original URL of the deployable unit jar
	 * @param deploymentRoot
	 *            the root dir where this du will be installed
	 * @param componentRepository
	 *            the repository to retrieve components
	 * @return
	 * @throws DeploymentException
	 */
	public DeployableUnit build(String url, File deploymentRoot,
			ComponentRepository componentRepository)
			throws DeploymentException, AlreadyDeployedException,
			MalformedURLException {

		if (logger.isDebugEnabled()) {
			logger.debug("Building DU from " + url);
		}

		DeployableUnitID deployableUnitID = new DeployableUnitID(url);

		if (deploymentRoot == null) {
			throw new NullPointerException("null deploymentRoot");
		}

		URL sourceUrl = new URL(url);

		// create jar file
		JarFile deployableUnitJar = null;
		try {
			deployableUnitJar = new JarFile(sourceUrl.getFile());
		} catch (IOException e) {
			throw new DeploymentException(
					"Failed to open DU file as JAR file: "
							+ sourceUrl.getFile(), e);
		}

		if (deployableUnitJar == null) {
			throw new NullPointerException("null deployableUnitJar");
		}

		// get and parse du descriptor
		JarEntry duXmlEntry = deployableUnitJar
				.getJarEntry("META-INF/deployable-unit.xml");
		if (duXmlEntry == null) {
			throw new DeploymentException(
					"META-INF/deployable-unit.xml was not found in "
							+ deployableUnitJar.getName());
		}
		DeployableUnitDescriptorFactory descriptorFactory = new DeployableUnitDescriptorFactory();
		DeployableUnitDescriptorImpl deployableUnitDescriptor = null;
		try {
			deployableUnitDescriptor = descriptorFactory
					.parse(deployableUnitJar.getInputStream(duXmlEntry));
		} catch (IOException e) {
			try {
				deployableUnitJar.close();
			} catch (IOException e1) {
				logger.error(e.getMessage(), e);
			}
			throw new DeploymentException(
					"Failed to get DU descriptor DU inputstream from JAR file "
							+ sourceUrl.getFile(), e);
		}

		// create the du dir
		File deploymentDir = createTempDUDeploymentDir(deploymentRoot,
				deployableUnitID);

		// build du object
		DeployableUnit deployableUnit = null;

		try {

			deployableUnit = new DeployableUnit(deployableUnitID,
					deployableUnitDescriptor, componentRepository,
					deploymentDir);

			// build each du jar component
			for (String jarFileName : deployableUnitDescriptor.getJarEntries()) {
				for (SleeComponent sleeComponent : duComponentBuilder
						.buildComponents(jarFileName, deployableUnitJar,
								deployableUnit.getDeploymentDir())) {
					sleeComponent.setDeployableUnit(deployableUnit);
					if (componentRepository.isInstalled(sleeComponent
							.getComponentID())) {
						throw new AlreadyDeployedException("Component "
								+ sleeComponent.getComponentID()
								+ " already deployed");
					}
					sleeComponent.setDeploymentUnitSource(jarFileName);
				}
			}

			// build each du service component
			for (String serviceDescriptorFileName : deployableUnitDescriptor
					.getServiceEntries()) {
				for (ServiceComponent serviceComponent : duServiceComponentBuilder
						.buildComponents(serviceDescriptorFileName,
								deployableUnitJar)) {
					serviceComponent.setDeployableUnit(deployableUnit);
					if (componentRepository.isInstalled(serviceComponent
							.getComponentID())) {
						throw new AlreadyDeployedException("Component "
								+ serviceComponent.getComponentID()
								+ " already deployed");
					}
					// set the direct reference to the sbb component
					serviceComponent.setRootSbbComponent(deployableUnit
							.getDeployableUnitRepository().getComponentByID(
									serviceComponent.getDescriptor()
											.getRootSbbID()));
					serviceComponent
							.setDeploymentUnitSource(serviceDescriptorFileName);
				}
			}

			// get a set with all components of the DU
			Set<SleeComponent> duComponentsSet = deployableUnit
					.getDeployableUnitComponents();

			// now that all components are built we need to , if that's the case
			for (SleeComponent sleeComponent : duComponentsSet) {
				// check if all
				// dependencies are available
				checkDependencies(sleeComponent, deployableUnit);
				// build its class loader
				createClassLoader(sleeComponent);
				// load the provided classes for the component
				loadAndSetNonGeneratedComponentClasses(sleeComponent);
			}
			//boolean secEnabled = SleeContainer.isSecurityEnabled();
			// validate each component
			for (SleeComponent sleeComponent : duComponentsSet) {
				ClassLoader componentClassLoader = sleeComponent
				.getClassLoader();
				ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
				try {
					if (componentClassLoader != null) {
						Thread.currentThread().setContextClassLoader(
								componentClassLoader);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Validating " + sleeComponent);
					}
					if (!sleeComponent.validate()) {
						throw new DeploymentException(
								sleeComponent.toString()
								+ " validation failed, check logs for errors found");
					}
					//Make permissions object, this instruments codebase etc, and store POJOs in component.
					//if(secEnabled)
					//{
						sleeComponent.processSecurityPermissions();
					//}
				} catch (Throwable e) {
					throw new DeploymentException("failed to validate "
							+ sleeComponent, e);
				} finally {
					if (componentClassLoader != null) {
						Thread.currentThread().setContextClassLoader(
								oldClassLoader);
					}
				}
			}

			try {
				deployableUnitJar.close();
			} catch (IOException e) {
				logger.error("failed to close deployable jar from " + url, e);
			}

			return deployableUnit;
		} catch (Throwable e) {
			if (deployableUnit != null) {
				if (logger.isInfoEnabled()) {
					logger
							.info(
									"Undeploying deployable unit due to building error",
									e);
				}
				deployableUnit.undeploy();
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Deleting deployable unit temp dir "
							+ deploymentDir);
				}
				deploymentDir.delete();
			}
			if (e instanceof DeploymentException) {
				throw (DeploymentException) e;
			} else if (e instanceof AlreadyDeployedException) {
				throw (AlreadyDeployedException) e;
			} else {
				throw new DeploymentException(
						"failed to build deployable unit", e);
			}
		}
	}

	

	public static void createClassLoader(SleeComponent component) {

		URLClassLoaderDomain classLoaderDomain = component.getClassLoaderDomain();
		if (classLoaderDomain != null) {
			// add all dependency domains to the component domain
			for (ComponentID componentID : component.getDependenciesSet()) {
				SleeComponent dependency = null;
				if (componentID instanceof EventTypeID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(EventTypeID) componentID);
				} else if (componentID instanceof LibraryID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(LibraryID) componentID);
				} else if (componentID instanceof ProfileSpecificationID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ProfileSpecificationID) componentID);
				} else if (componentID instanceof ResourceAdaptorID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ResourceAdaptorID) componentID);
				} else if (componentID instanceof ResourceAdaptorTypeID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ResourceAdaptorTypeID) componentID);
				} else if (componentID instanceof SbbID) {
					dependency = component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(SbbID) componentID);
				}
				if (dependency != null && dependency.getClassLoaderDomain() != null) {					
					classLoaderDomain.getDependencies().add(dependency.getClassLoaderDomain());
					if (logger.isDebugEnabled()) {
						logger.debug(classLoaderDomain+" added "+dependency.getClassLoaderDomain()+ " in dependencies");
					}
				}
				else {
					throw new SLEEException(component.toString()+" dependency "+componentID+" not found or doesn't have class loading domain");
				}
			}
			// create class loader
			component.setClassLoader(new ComponentClassLoader(component.getComponentID(),classLoaderDomain));			
		}

	}

	/**
	 * Loads all non SLEE generated classes from the component class loader to
	 * the component, those will be needed for validation or runtime purposes
	 * 
	 * @param sleeComponent
	 * @throws DeploymentException
	 */
	private void loadAndSetNonGeneratedComponentClasses(
			SleeComponent sleeComponent) throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Loading classes for component " + sleeComponent);
		}
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			ComponentClassLoader componentClassLoader = sleeComponent
					.getClassLoader();
			if (componentClassLoader != null) {
				// change class loader
				Thread.currentThread().setContextClassLoader(
						componentClassLoader);
				// load and set non generated component classes
				if (sleeComponent instanceof EventTypeComponent) {
					EventTypeComponent component = (EventTypeComponent) sleeComponent;
					Class<?> eventTypeClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getEventClassName());
					component.setEventTypeClass(eventTypeClass);
				} else if (sleeComponent instanceof ProfileSpecificationComponent) {
					ProfileSpecificationComponent component = (ProfileSpecificationComponent) sleeComponent;
					Class<?> profileCmpInterfaceClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileClasses()
									.getProfileCMPInterface()
									.getProfileCmpInterfaceName());
					component
							.setProfileCmpInterfaceClass(profileCmpInterfaceClass);
					if (component.getDescriptor().getProfileClasses()
							.getProfileLocalInterface() != null) {
						Class<?> profileLocalInterfaceClass = componentClassLoader
								.loadClass(component.getDescriptor()
										.getProfileClasses()
										.getProfileLocalInterface()
										.getProfileLocalInterfaceName());
						component
								.setProfileLocalInterfaceClass(profileLocalInterfaceClass);
					}
					if (component.getDescriptor().getProfileClasses()
							.getProfileManagementInterface() != null) {
						Class<?> profileManagementInterfaceClass = componentClassLoader
								.loadClass(component.getDescriptor()
										.getProfileClasses()
										.getProfileManagementInterface()
										.getProfileManagementInterfaceName());
						component
								.setProfileManagementInterfaceClass(profileManagementInterfaceClass);
					}
					if (component.getDescriptor().getProfileClasses()
							.getProfileAbstractClass() != null) {
						Class<?> profileAbstractClass = componentClassLoader
								.loadClass(component.getDescriptor()
										.getProfileClasses()
										.getProfileAbstractClass()
										.getProfileAbstractClassName());
						component.setProfileAbstractClass(profileAbstractClass);
					}
					MProfileTableInterface mProfileTableInterface = component
							.getDescriptor().getProfileClasses()
							.getProfileTableInterface();
					if (mProfileTableInterface != null) {
						component
								.setProfileTableInterfaceClass(componentClassLoader
										.loadClass(mProfileTableInterface
												.getProfileTableInterfaceName()));
					}
					MUsageParametersInterface mUsageParametersInterface = component
							.getDescriptor().getProfileClasses()
							.getProfileUsageParameterInterface();
					if (mUsageParametersInterface != null) {
						component
								.setUsageParametersInterface(componentClassLoader
										.loadClass(mUsageParametersInterface
												.getUsageParametersInterfaceName()));
					}
				} else if (sleeComponent instanceof ResourceAdaptorComponent) {
					ResourceAdaptorComponent component = (ResourceAdaptorComponent) sleeComponent;
					Class<?> resourceAdaptorClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getResourceAdaptorClassName());
					component.setResourceAdaptorClass(resourceAdaptorClass);
					MUsageParametersInterface mUsageParametersInterface = component
							.getDescriptor()
							.getResourceAdaptorUsageParametersInterface();
					if (mUsageParametersInterface != null) {
						component
								.setUsageParametersInterface(componentClassLoader
										.loadClass(mUsageParametersInterface
												.getUsageParametersInterfaceName()));
					}
				} else if (sleeComponent instanceof ResourceAdaptorTypeComponent) {
					ResourceAdaptorTypeComponent component = (ResourceAdaptorTypeComponent) sleeComponent;
					if (component.getDescriptor()
							.getActivityContextInterfaceFactoryInterface() != null) {
						Class<?> activityContextInterfaceFactoryInterface = componentClassLoader
								.loadClass(component
										.getDescriptor()
										.getActivityContextInterfaceFactoryInterface()
										.getActivityContextInterfaceFactoryInterfaceName());
						component
								.setActivityContextInterfaceFactoryInterface(activityContextInterfaceFactoryInterface);
					}
				} else if (sleeComponent instanceof SbbComponent) {
					SbbComponent component = (SbbComponent) sleeComponent;
					// before loading the abstract class, we may have to decorate it
					boolean decoratedClass = new SbbAbstractClassDecorator(component).decorateAbstractSbb();
					Class<?> abstractSbbClass = null;
					if (decoratedClass) {
						// need to ensure we load the class from disk, not one coming from SLEE shared class loading domain
						 abstractSbbClass = componentClassLoader
							.loadClassLocally(component.getDescriptor()
									.getSbbClasses().getSbbAbstractClass()
									.getSbbAbstractClassName());
					}
					else {
						 abstractSbbClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getSbbClasses().getSbbAbstractClass()
									.getSbbAbstractClassName());
					}
					component.setAbstractSbbClass(abstractSbbClass);
					MSbbLocalInterface mSbbLocalInterface = component
							.getDescriptor().getSbbClasses()
							.getSbbLocalInterface();
					if (mSbbLocalInterface != null) {
						component
								.setSbbLocalInterfaceClass(componentClassLoader
										.loadClass(mSbbLocalInterface
												.getSbbLocalInterfaceName()));
					}
					MSbbActivityContextInterface mSbbActivityContextInterface = component
							.getDescriptor().getSbbClasses()
							.getSbbActivityContextInterface();
					if (mSbbActivityContextInterface != null) {
						component
								.setActivityContextInterface(componentClassLoader
										.loadClass(mSbbActivityContextInterface
												.getInterfaceName()));
					}
					MUsageParametersInterface mUsageParametersInterface = component
							.getDescriptor().getSbbClasses()
							.getSbbUsageParametersInterface();
					if (mUsageParametersInterface != null) {
						component
								.setUsageParametersInterface(componentClassLoader
										.loadClass(mUsageParametersInterface
												.getUsageParametersInterfaceName()));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new DeploymentException("Component "
					+ sleeComponent.getComponentID()
					+ " requires a class that was not found", e);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	/**
	 * Checks if all dependencies of a DU component exists
	 * 
	 * @param sleeComponent
	 * @param deployableUnit
	 * @throws DependencyException
	 *             if a dependency is missing
	 */
	private void checkDependencies(SleeComponent sleeComponent,
			DeployableUnit deployableUnit) throws DependencyException {
		for (ComponentID componentID : sleeComponent.getDependenciesSet()) {
			if (componentID instanceof EventTypeID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((EventTypeID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof LibraryID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((LibraryID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof ProfileSpecificationID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((ProfileSpecificationID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof ResourceAdaptorID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((ResourceAdaptorID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof ResourceAdaptorTypeID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((ResourceAdaptorTypeID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof SbbID) {
				if (deployableUnit.getDeployableUnitRepository()
						.getComponentByID((SbbID) componentID) == null) {
					throw new DependencyException(
							"Component "
									+ sleeComponent.getComponentID()
									+ " depends on "
									+ componentID
									+ " which is not available in the component repository or in the deployable unit");
				}
			} else if (componentID instanceof ServiceID) {
				throw new SLEEException(
						"Component "
								+ sleeComponent.getComponentID()
								+ " depends on a service component "
								+ componentID
								+ " which is not available in the component repository or in the deployable unit");
			}
		}
	}

	/**
	 * Creates the directory that will be used for unpacking the child jars for
	 * a given DU.
	 * 
	 * @param rootDir
	 * @param sourceUrl
	 * @throws SLEEException
	 *             if the dir can't be created
	 * @return
	 */
	private File createTempDUDeploymentDir(File deploymentRoot,
			DeployableUnitID deployableUnitID) {
		try {
			// first create a dummy file to gurantee uniqueness. I would have
			// been nice if the File class had a createTempDir() method
			// IVELIN -- do not use jarName here because windows cannot see the
			// path (exceeds system limit)
			File tempFile = File.createTempFile("mobicents-slee-du-", "",
					deploymentRoot);
			File tempDUDeploymentDir = new File(tempFile.getAbsolutePath()
					+ "-contents");
			if (!tempDUDeploymentDir.exists()) {
				tempDUDeploymentDir.mkdirs();
			} else {
				throw new SLEEException(
						"Dir "
								+ tempDUDeploymentDir
								+ " already exists, unable to create deployment dir for DU "
								+ deployableUnitID);
			}
			tempFile.delete();
			return tempDUDeploymentDir;
		} catch (IOException e) {
			throw new SLEEException("Failed to create deployment dir for DU "
					+ deployableUnitID, e);
		}
	}

}