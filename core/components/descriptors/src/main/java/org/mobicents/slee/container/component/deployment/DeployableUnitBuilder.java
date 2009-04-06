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
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.management.ObjectName;
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
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.mobicents.slee.container.component.ComponentJarClassLoaderDomain;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponent;
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

			// now that all components are built we need to check if all
			// dependencies are available
			for (SleeComponent sleeComponent : duComponentsSet) {
				checkDependencies(sleeComponent, deployableUnit);
			}

			// if component has a deployment dir and the du ha then create class
			// loader
			// domain and register the policy
			ClassLoaderSystem classLoaderSystem = ClassLoaderSystem
					.getInstance();
			for (SleeComponent component : duComponentsSet) {
				File componentDeploymentDir = component.getDeploymentDir();
				if (componentDeploymentDir != null) {
					String domainName = getSafeDomainName(componentDeploymentDir
							.getAbsolutePath());
					ComponentJarClassLoaderDomain classLoaderDomain = deployableUnit
							.getClassLoaderDomain(domainName);
					if (classLoaderDomain == null) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Creating class loading domain for deploy dir "
											+ domainName);
						}
						// create domain
						classLoaderDomain = new ComponentJarClassLoaderDomain(
								domainName);
						classLoaderDomain.register();
						deployableUnit
								.addClassLoaderDomain(classLoaderDomain);
					}
					component
							.setClassLoader(classLoaderDomain.getClassLoader());
				}
			}

			// now that all the components class loaders were created, let's add
			// the
			// class loader domains of the components dependencies
			for (SleeComponent component : duComponentsSet) {
				if (component.getDeploymentDir() != null) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Adding class loading domains from dependencies to class loading domain of component "
										+ component);
					}
					ComponentJarClassLoaderDomain domain = deployableUnit
							.getClassLoaderDomain(component
									.getDeploymentDir().getAbsolutePath());
					HashSet<String> domainsProcessed = new HashSet<String>();
					domainsProcessed.add(domain.getName());
					addDependenciesClassLoadingDomains(component, domain,
							classLoaderSystem, domainsProcessed, deployableUnit);
				}
			}

			// for each component load and set its non generated classes
			for (SleeComponent sleeComponent : duComponentsSet) {
				loadAndSetNonGeneratedComponentClasses(sleeComponent);
			}

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
							.info("Undeploying deployable unit due to building error");
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

	/*
	 * FIXME jboss ClassLoaderSystem has a bug in producing the objectname for the domain, till it is solved we must handle it here
	 * @param name
	 * @return
	 */
	private String getSafeDomainName(String name) {
		String domainName = ObjectName.quote(name);
		return domainName.substring(1,domainName.length()-1);
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
			ClassLoader componentClassLoader = sleeComponent.getClassLoader();
			if (componentClassLoader != null) {
				// change class loader
				Thread.currentThread().setContextClassLoader(
						componentClassLoader);
				// load and set non generated component classes
				if (sleeComponent instanceof EventTypeComponent) {
					EventTypeComponent component = (EventTypeComponent) sleeComponent;
					Class eventTypeClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getEventClassName());
					component.setEventTypeClass(eventTypeClass);
				} else if (sleeComponent instanceof ProfileSpecificationComponent) {
					ProfileSpecificationComponent component = (ProfileSpecificationComponent) sleeComponent;
					Class profileCmpInterfaceClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileClasses()
									.getProfileCMPInterface()
									.getProfileCmpInterfaceName());
					component
							.setProfileCmpInterfaceClass(profileCmpInterfaceClass);
					Class profileLocalInterfaceClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileClasses()
									.getProfileLocalInterface()
									.getProfileLocalInterfaceName());
					component
							.setProfileLocalInterfaceClass(profileLocalInterfaceClass);
					Class profileManagementInterfaceClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileClasses()
									.getProfileManagementInterface()
									.getProfileManagementInterfaceName());
					component
							.setProfileManagementInterfaceClass(profileManagementInterfaceClass);
					Class profileAbstractClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileClasses()
									.getProfileAbstractClass()
									.getProfileAbstractClassName());
					component.setProfileAbstractClass(profileAbstractClass);
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
					Class resourceAdaptorClass = componentClassLoader
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
					Class activityContextInterfaceFactoryInterface = componentClassLoader
							.loadClass(component
									.getDescriptor()
									.getActivityContextInterfaceFactoryInterface()
									.getActivityContextInterfaceFactoryInterfaceName());
					component
							.setActivityContextInterfaceFactoryInterface(activityContextInterfaceFactoryInterface);
				} else if (sleeComponent instanceof SbbComponent) {
					SbbComponent component = (SbbComponent) sleeComponent;
					Class abstractSbbClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getSbbClasses().getSbbAbstractClass()
									.getSbbAbstractClassName());
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
	 * Adds all dependencies class loader domains to the specified component jar
	 * class load domain domain
	 * 
	 * @param sleeComponent
	 * @param domainToAddDependencies
	 * @param classLoaderSystem
	 * @param componentsProcessed
	 *            the components already processed, to avoid loops on circular
	 *            dependencies
	 */
	private void addDependenciesClassLoadingDomains(
			SleeComponent sleeComponent,
			ComponentJarClassLoaderDomain domainToAddDependencies,
			ClassLoaderSystem classLoaderSystem,
			Set<String> componentsProcessed, DeployableUnit deployableUnit) {
		for (ComponentID componentID : sleeComponent.getDependenciesSet()) {
			SleeComponent component = null;
			if (componentID instanceof EventTypeID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(EventTypeID) componentID);
			} else if (componentID instanceof LibraryID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(LibraryID) componentID);
			} else if (componentID instanceof ProfileSpecificationID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(ProfileSpecificationID) componentID);
			} else if (componentID instanceof ResourceAdaptorID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(ResourceAdaptorID) componentID);
			} else if (componentID instanceof ResourceAdaptorTypeID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(ResourceAdaptorTypeID) componentID);
			} else if (componentID instanceof SbbID) {
				component = sleeComponent.getDeployableUnit()
						.getDeployableUnitRepository().getComponentByID(
								(SbbID) componentID);
			}
			if (component != null && component.getDeploymentDir() != null) {
				String domainName = component.getDeploymentDir()
						.getAbsolutePath();
				if (componentsProcessed.add(domainName)) {
					// add the referenced component domain
					ComponentJarClassLoaderDomain refDomain = deployableUnit
							.getClassLoaderDomain(domainName);
					if (refDomain != null) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Adding class loading domain from component "
											+ component
											+ " to class loading domain "
											+ domainToAddDependencies);
						}
						domainToAddDependencies.addDependencyDomain(refDomain);
					}
					// and add the component dependencies too
					addDependenciesClassLoadingDomains(component,
							domainToAddDependencies, classLoaderSystem,
							componentsProcessed, deployableUnit);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Class loading domain for component "
								+ component + " already in domain "
								+ domainToAddDependencies);
					}
				}
			} else {
				logger
						.warn("Unable to add domain for component "
								+ component
								+ " in domain "
								+ domainToAddDependencies
								+ ". Component not found in the deployable unit component repository");
			}
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