package org.mobicents.slee.container.component.deployment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import org.mobicents.slee.container.component.AbstractSleeComponent;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponentImpl;
import org.mobicents.slee.container.component.ProfileSpecificationComponentImpl;
import org.mobicents.slee.container.component.ResourceAdaptorComponentImpl;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponentImpl;
import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.ServiceComponentImpl;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.classloading.ComponentClassLoader;
import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.component.deployment.classloading.ComponentClassLoaderImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.du.DeployableUnitBuilder;
import org.mobicents.slee.container.component.sbb.SbbLocalInterfaceDescriptor;

public class DeployableUnitBuilderImpl implements DeployableUnitBuilder {

	private static final Logger logger = Logger
			.getLogger(DeployableUnitBuilderImpl.class);

	private final DeployableUnitJarComponentBuilder duComponentBuilder;
	private final DeployableUnitServiceComponentBuilder duServiceComponentBuilder;

	private final ComponentManagementImpl componentManagement;

	/**
	 * 
	 */
	public DeployableUnitBuilderImpl(ComponentManagementImpl componentManagement) {
		this.componentManagement = componentManagement;
		this.duComponentBuilder = new DeployableUnitJarComponentBuilder(componentManagement);
		this.duServiceComponentBuilder = new DeployableUnitServiceComponentBuilder(componentManagement);
	}
	
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
	public DeployableUnitImpl build(String url, File deploymentRoot,
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

		// support remote deployment
		if(sourceUrl.getProtocol().equals("http") || sourceUrl.getProtocol().equals("https")) {
		  try {
		    // Fetch the remote file to a temporary location
		    File downloadedFile = downloadRemoteDU(sourceUrl, deploymentRoot);

		    // Update the pointers from URL and String
		    sourceUrl = downloadedFile.toURI().toURL();
		    url = sourceUrl.toString();
		  }
		  catch (Exception e) {
		    throw new DeploymentException("Failed to retrieve remote DU file : " + sourceUrl.getFile(), e);
		  }
		}

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
		DeployableUnitDescriptorFactoryImpl descriptorFactory = componentManagement.getComponentDescriptorFactory().getDeployableUnitDescriptorFactory();
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
		DeployableUnitImpl deployableUnit = null;

		try {

			deployableUnit = new DeployableUnitImpl(deployableUnitID,
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
				for (ServiceComponentImpl serviceComponent : duServiceComponentBuilder
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

			// now that all components are built we need to
			for (SleeComponent sleeComponent : duComponentsSet) {
				// check if all
				// dependencies are available
				checkDependencies(sleeComponent, deployableUnit);
				// build its class loader
				createClassLoader((AbstractSleeComponent) sleeComponent);
			}

			// load the provided classes for the component
			for (SleeComponent sleeComponent : duComponentsSet) {
				loadAndSetNonGeneratedComponentClasses((AbstractSleeComponent) sleeComponent);
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

	

	public static void createClassLoader(AbstractSleeComponent component) {

		URLClassLoaderDomain classLoaderDomain = component.getClassLoaderDomain();
		if (classLoaderDomain != null) {
			// add all dependency domains to the component domain
			for (ComponentID componentID : component.getDependenciesSet()) {
				AbstractSleeComponent dependency = null;
				if (componentID instanceof EventTypeID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(EventTypeID) componentID);
				} else if (componentID instanceof LibraryID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(LibraryID) componentID);
				} else if (componentID instanceof ProfileSpecificationID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ProfileSpecificationID) componentID);
				} else if (componentID instanceof ResourceAdaptorID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ResourceAdaptorID) componentID);
				} else if (componentID instanceof ResourceAdaptorTypeID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(ResourceAdaptorTypeID) componentID);
				} else if (componentID instanceof SbbID) {
					dependency = (AbstractSleeComponent) component.getDeployableUnit()
							.getDeployableUnitRepository().getComponentByID(
									(SbbID) componentID);
				}
				if (dependency != null && dependency.getClassLoaderDomain() != null) {					
					classLoaderDomain.getDependencies().add(dependency.getClassLoaderDomain());
					if (logger.isTraceEnabled()) {
						logger.trace(classLoaderDomain+" added "+dependency.getClassLoaderDomain()+ " in dependencies");
					}
				}
				else {
					throw new SLEEException(component.toString()+" dependency "+componentID+" not found or doesn't have class loading domain");
				}
			}
			// create class loader
			component.setClassLoader(new ComponentClassLoaderImpl(component.getComponentID(),classLoaderDomain));			
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
			AbstractSleeComponent sleeComponent) throws DeploymentException {

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
				if (sleeComponent instanceof EventTypeComponentImpl) {
					EventTypeComponentImpl component = (EventTypeComponentImpl) sleeComponent;
					Class<?> eventTypeClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getEventClassName());
					component.setEventTypeClass(eventTypeClass);
				} else if (sleeComponent instanceof ProfileSpecificationComponentImpl) {
					ProfileSpecificationComponentImpl component = (ProfileSpecificationComponentImpl) sleeComponent;
					Class<?> profileCmpInterfaceClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileCMPInterface()
									.getProfileCmpInterfaceName());
					component
							.setProfileCmpInterfaceClass(profileCmpInterfaceClass);
					if (component.getDescriptor()
							.getProfileLocalInterface() != null) {
						Class<?> profileLocalInterfaceClass = componentClassLoader
								.loadClass(component.getDescriptor()
										.getProfileLocalInterface()
										.getProfileLocalInterfaceName());
						component
								.setProfileLocalInterfaceClass(profileLocalInterfaceClass);
					}
					if (component.getDescriptor()
							.getProfileManagementInterface() != null) {
						Class<?> profileManagementInterfaceClass = componentClassLoader
								.loadClass(component.getDescriptor()
										.getProfileManagementInterface());
						component
								.setProfileManagementInterfaceClass(profileManagementInterfaceClass);
					}
					if (component.getDescriptor()
							.getProfileAbstractClass() != null) {
						boolean decoratedClass = new ProfileAbstractClassDecorator(component).decorateAbstractClass();
						Class<?> profileAbstractClass = null;
						if (decoratedClass) {
							// need to ensure we load the class from disk, not one coming from SLEE shared class loading domain
							profileAbstractClass = componentClassLoader
							.loadClassLocally(component.getDescriptor()
									.getProfileAbstractClass()
									.getProfileAbstractClassName());
						}
						else {
							profileAbstractClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getProfileAbstractClass()
									.getProfileAbstractClassName());
						}
						component.setProfileAbstractClass(profileAbstractClass);
					}
					String mProfileTableInterface = component
							.getDescriptor()
							.getProfileTableInterface();
					if (mProfileTableInterface != null) {
						component
								.setProfileTableInterfaceClass(componentClassLoader
										.loadClass(mProfileTableInterface));
					}
					UsageParametersInterfaceDescriptor mUsageParametersInterface = component
							.getDescriptor()
							.getProfileUsageParameterInterface();
					if (mUsageParametersInterface != null) {
						component
								.setUsageParametersInterface(componentClassLoader
										.loadClass(mUsageParametersInterface
												.getUsageParametersInterfaceName()));
					}
				} else if (sleeComponent instanceof ResourceAdaptorComponentImpl) {
					ResourceAdaptorComponentImpl component = (ResourceAdaptorComponentImpl) sleeComponent;
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
				} else if (sleeComponent instanceof ResourceAdaptorTypeComponentImpl) {
					ResourceAdaptorTypeComponentImpl component = (ResourceAdaptorTypeComponentImpl) sleeComponent;
					if (component.getDescriptor()
							.getActivityContextInterfaceFactoryInterface() != null) {
						Class<?> activityContextInterfaceFactoryInterface = componentClassLoader
								.loadClass(component
										.getDescriptor()
										.getActivityContextInterfaceFactoryInterface());
						component
								.setActivityContextInterfaceFactoryInterface(activityContextInterfaceFactoryInterface);
					}
				} else if (sleeComponent instanceof SbbComponentImpl) {
					SbbComponentImpl component = (SbbComponentImpl) sleeComponent;
					// before loading the abstract class, we may have to decorate it
					boolean decoratedClass = new SbbAbstractClassDecorator(component).decorateAbstractSbb();
					Class<?> abstractSbbClass = null;
					if (decoratedClass) {
						// need to ensure we load the class from disk, not one coming from SLEE shared class loading domain
						 abstractSbbClass = componentClassLoader
							.loadClassLocally(component.getDescriptor()
									.getSbbAbstractClass()
									.getSbbAbstractClassName());
					}
					else {
						 abstractSbbClass = componentClassLoader
							.loadClass(component.getDescriptor()
									.getSbbAbstractClass()
									.getSbbAbstractClassName());
					}
					component.setAbstractSbbClass(abstractSbbClass);
					SbbLocalInterfaceDescriptor mSbbLocalInterface = component
							.getDescriptor()
							.getSbbLocalInterface();
					if (mSbbLocalInterface != null) {
						component
								.setSbbLocalInterfaceClass(componentClassLoader
										.loadClass(mSbbLocalInterface
												.getSbbLocalInterfaceName()));
					}
					String mSbbActivityContextInterface = component
							.getDescriptor()
							.getSbbActivityContextInterface();
					if (mSbbActivityContextInterface != null) {
						component
								.setActivityContextInterface(componentClassLoader
										.loadClass(mSbbActivityContextInterface));
					}
					UsageParametersInterfaceDescriptor mUsageParametersInterface = component
							.getDescriptor()
							.getSbbUsageParametersInterface();
					if (mUsageParametersInterface != null) {
						component
								.setUsageParametersInterface(componentClassLoader
										.loadClass(mUsageParametersInterface
												.getUsageParametersInterfaceName()));
					}
				}
			}
		} catch (NoClassDefFoundError e) {
			throw new DeploymentException("Component "
					+ sleeComponent.getComponentID()
					+ " requires a class that was not found", e);
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
			DeployableUnitImpl deployableUnit) throws DependencyException {
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

	private File downloadRemoteDU(URL duURL, File deploymentRoot) throws Exception {
	  InputStream in = null;
	  OutputStream out = null;

	  try {
	    String fileWithPath = duURL.getFile();
	    int start = fileWithPath.lastIndexOf('/') + 1;
	    String filename = Math.abs(duURL.hashCode()) + "_" + fileWithPath.substring(start, fileWithPath.length());

	    File tempFile = new File(deploymentRoot, filename);

	    out = new BufferedOutputStream(new FileOutputStream(tempFile));
	    URLConnection conn = duURL.openConnection();
	    in = conn.getInputStream();

	    // Get the data
	    byte[] buffer = new byte[1024];
	    int numRead;
	    while ((numRead = in.read(buffer)) != -1) {
	      out.write(buffer, 0, numRead);
	    }
	    // Done! Successful.
	    return tempFile;
	  }
	  finally {
	    try {
	      if (in != null) {
	        in.close();
	        in = null;
	      }
	      if (out != null) {
	        out.close();
	        out = null;
	      }
	    }
	    catch (IOException ioe) {
	      // Shouldn't happen, let's ignore.
	    }
	  }
	}
}