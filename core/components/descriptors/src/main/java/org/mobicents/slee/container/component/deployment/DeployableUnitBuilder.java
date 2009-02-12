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
import java.io.InputStream;
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
import javax.slee.management.DependencyException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.DeployableUnit;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DefaultEntityResolver;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl;
import org.w3c.dom.Document;

public class DeployableUnitBuilder {
    
	private static final Logger logger = Logger.getLogger(DeployableUnitBuilder.class);
	
	private static final DeployableUnitJarComponentBuilder duComponentBuilder = new DeployableUnitJarComponentBuilder();
	private static final DeployableUnitServiceComponentBuilder duServiceComponentBuilder = new DeployableUnitServiceComponentBuilder();
    
    /**
     * Installs a JAIN SLEE DU.
     * 
     * @param sourceUrl the original URL of the deployable unit jar
     * @param deploymentRoot the root dir where this du will be installed
     * @param componentRepository the repository to retrieve components
     * @return
     * @throws DeploymentException
     */
    public DeployableUnit build(String url,File deploymentRoot, ComponentRepository componentRepository) throws DeploymentException,MalformedURLException {
        
    	if(logger.isDebugEnabled()){
            logger.debug("Installing DU from " + url);
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
    	JarEntry duXmlEntry = deployableUnitJar.getJarEntry("META-INF/deployable-unit.xml");
        if (duXmlEntry == null) {
        	throw new DeploymentException("META-INF/deployable-unit.xml was not found in " + deployableUnitJar.getName());
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new SLEEException("failed to create DOM builder factory when installing "+deployableUnitID,e);
		}
		builder.setEntityResolver(new DefaultEntityResolver(Thread.currentThread()
				.getContextClassLoader()));
		Document duXmlDocument = null;
		InputStream duXmlInputStream = null;
		try {
			duXmlInputStream = deployableUnitJar.getInputStream(duXmlEntry);
			duXmlDocument = builder.parse(duXmlInputStream);
		} catch (Exception e) {
			throw new DeploymentException("failed to parse deployable unit xml descriptor when installing "+deployableUnitID,e);
		}
		finally {
			if (duXmlInputStream != null) {
				try {
					duXmlInputStream.close();
				} catch (Exception e) {
					logger.error("failed to close du descriptor inputstream ",e);
				}
			}
		}
		DeployableUnitDescriptorImpl deployableUnitDescriptor = new DeployableUnitDescriptorImpl(duXmlDocument);
		
		// create the du dir
		File deploymentDir = createTempDUDeploymentDir(deploymentRoot, deployableUnitID);
		
		// build du object
		DeployableUnit deployableUnit = new DeployableUnit(deployableUnitID,deployableUnitDescriptor,componentRepository,deploymentDir);
		
		// build each du jar component
		for (String jarFileName : deployableUnitDescriptor.getJarEntries()) {			
			SleeComponent sleeComponent = duComponentBuilder.buildComponent(jarFileName, deployableUnitJar, deployableUnit.getDeploymentDir(), builder);
			sleeComponent.setDeployableUnit(deployableUnit);
		}
		
		// build each du service component
		for (String serviceDescriptorFileName : deployableUnitDescriptor.getServiceEndtries()) {			
			ServiceComponent serviceComponent = duServiceComponentBuilder.buildComponent(serviceDescriptorFileName, deployableUnitJar, builder);
			serviceComponent.setDeployableUnit(deployableUnit);
		}
		
		// get a set with all components of the DU
		Set<SleeComponent> duComponentsSet = deployableUnit.getDeployableUnitComponents();
		
		// now that all components are built we need to check if all dependencies are available
		for (SleeComponent sleeComponent : duComponentsSet) {
			 checkDependencies(sleeComponent, deployableUnit);
		}
				
		// if component has a class loader policy then create class loader domain and register the policy
		ClassLoaderSystem classLoaderSystem = ClassLoaderSystem.getInstance();
		for (SleeComponent sleeComponent : duComponentsSet) {
			ClassLoaderPolicy policy = sleeComponent.getClassLoaderPolicy();
			if (policy != null) {
				ClassLoaderDomain domain = classLoaderSystem.createAndRegisterDomain(sleeComponent.getDeployableUnit().getDeployableUnitID().toString()+sleeComponent.getComponentID(), ParentPolicy.AFTER, classLoaderSystem.getDefaultDomain());
				ClassLoader classLoader = classLoaderSystem.registerClassLoaderPolicy(domain, policy);
				sleeComponent.setClassLoader(classLoader);
			}
		}
		
		// now that all components have class loading domains, let's add the policies of the components it depends
		for (SleeComponent sleeComponent : duComponentsSet) {
			 addDependenciesClassLoadingPolicies(sleeComponent,sleeComponent.getClassLoaderDomain(),classLoaderSystem);
		}
		
		// for each component load and set its non generated classes
		for (SleeComponent sleeComponent : duComponentsSet) {
			loadAndSetNonGeneratedComponentClasses(sleeComponent);
		}
		
		// validate each component
		for (SleeComponent sleeComponent : duComponentsSet) {
			sleeComponent.validate();
		}
		
		// TODO generate any classes needed by the component
		
		try {
			deployableUnitJar.close();
		} catch (IOException e) {
			logger.error("failed to close deployable jar from "+url, e);
		}
		
		return deployableUnit;
    }

    /**
     * Loads all non SLEE generated classes from the component class loader to the component, those will be needed for validation or runtime purposes
     * @param sleeComponent
     * @throws DeploymentException
     */
	private void loadAndSetNonGeneratedComponentClasses(SleeComponent sleeComponent) throws DeploymentException {
		
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			ClassLoader componentClassLoader = sleeComponent.getClassLoader();
			if (componentClassLoader != null) {
				// change class loader
				Thread.currentThread().setContextClassLoader(componentClassLoader);
				// load and set non generated component classes
				if (sleeComponent instanceof EventTypeComponent) {
					EventTypeComponent component = (EventTypeComponent) sleeComponent;
					Class eventTypeClass = componentClassLoader.loadClass(component.getDescriptor().getEventClassName());
					component.setEventTypeClass(eventTypeClass);
				}				
				else if (sleeComponent instanceof ProfileSpecificationComponent) {
					ProfileSpecificationComponent component = (ProfileSpecificationComponent) sleeComponent;
					Class profileCmpInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName());
					component.setProfileCmpInterfaceClass(profileCmpInterfaceClass);
					Class profileLocalInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getProfileLocalInterface().getProfileLocalInterfaceName());
					component.setProfileLocalInterfaceClass(profileLocalInterfaceClass);
					Class profileManagementInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getProfileManagementInterface().getProfileManagementInterfaceName());
					component.setProfileManagementInterfaceClass(profileManagementInterfaceClass);
					Class profileAbstractClass = componentClassLoader.loadClass(component.getDescriptor().getProfileAbstractClass().getProfileAbstractClassName());
					component.setProfileAbstractClass(profileAbstractClass);
					Class profileTableInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getProfileTableInterface().getProfileTableInterfaceName());
					component.setProfileTableInterfaceClass(profileTableInterfaceClass);
					Class profileUsageInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getProfileUsageParameterInterface().getUsageParametersInterfaceName());
					component.setProfileUsageInterfaceClass(profileUsageInterfaceClass);
				}
				else if (sleeComponent instanceof ResourceAdaptorComponent) {
					ResourceAdaptorComponent component = (ResourceAdaptorComponent) sleeComponent;
					Class resourceAdaptorClass = componentClassLoader.loadClass(component.getDescriptor().getResourceAdaptorClassName());
					component.setResourceAdaptorClass(resourceAdaptorClass);
					Class resourceAdaptorUsageParametersInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getResourceAdaptorUsageParametersInterface().getUsageParametersInterfaceName());
					component.setResourceAdaptorUsageParametersInterfaceClass(resourceAdaptorUsageParametersInterfaceClass);
				}
				else if (sleeComponent instanceof ResourceAdaptorTypeComponent) {
					ResourceAdaptorTypeComponent component = (ResourceAdaptorTypeComponent) sleeComponent;
					Class activityContextInterfaceFactoryInterface = componentClassLoader.loadClass(component.getDescriptor().getActivityContextInterfaceFactoryInterface().getActivityContextInterfaceFactoryInterfaceName());
					component.setActivityContextInterfaceFactoryInterface(activityContextInterfaceFactoryInterface);
				}
				else if (sleeComponent instanceof SbbComponent) {
					SbbComponent component = (SbbComponent) sleeComponent;
					Class abstractSbbClass = componentClassLoader.loadClass(component.getDescriptor().getSbbAbstractClass().getSbbAbstractClassName());
					component.setAbstractSbbClass(abstractSbbClass);
					Class sbbLocalInterfaceClass = componentClassLoader.loadClass(component.getDescriptor().getSbbLocalInterface().getSbbLocalInterfaceName());
					component.setSbbLocalInterfaceClass(sbbLocalInterfaceClass);
					Class activityContextInterface = componentClassLoader.loadClass(component.getDescriptor().getSbbActivityContextInterface().getInterfaceName());
					component.setActivityContextInterface(activityContextInterface);
					Class usageParametersInterface = componentClassLoader.loadClass(component.getDescriptor().getSbbUsageParametersInterface().getUsageParametersInterfaceName());
					component.setUsageParametersInterface(usageParametersInterface);
				}
			}
		} catch (ClassNotFoundException e) {
			throw new DeploymentException("Component "+sleeComponent.getComponentID()+" requires a class that was not found",e);
		}
		finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	/**
	 * Adds all dependencies class loading policies to the a specific class load domain
	 * @param sleeComponent
	 * @param domainToAddPolicies
	 * @param classLoaderSystem
	 */
	private void addDependenciesClassLoadingPolicies(
			SleeComponent sleeComponent, ClassLoaderDomain domainToAddPolicies, ClassLoaderSystem classLoaderSystem) {
		for (ComponentID componentID : sleeComponent.getDependenciesSet()) {
			if (componentID instanceof EventTypeID) {
				SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((EventTypeID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
    		else if (componentID instanceof LibraryID) {
    			SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((LibraryID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
    		else if (componentID instanceof ProfileSpecificationID) {
    			SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((ProfileSpecificationID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
    		else if (componentID instanceof ResourceAdaptorID) {
    			SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((ResourceAdaptorID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
    		else if (componentID instanceof ResourceAdaptorTypeID) {
    			SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((ResourceAdaptorTypeID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
    		else if (componentID instanceof SbbID) {
    			SleeComponent component = sleeComponent.getDeployableUnit().getDeployableUnitRepository().getComponentByID((SbbID)componentID);
    			ClassLoaderPolicy policy = component.getClassLoaderPolicy();
				if (policy != null) {
					classLoaderSystem.registerClassLoaderPolicy(domainToAddPolicies, policy);
				}
				// and add the component dependencies too
				addDependenciesClassLoadingPolicies(component, domainToAddPolicies, classLoaderSystem);
    		}
		 }
	}

	/**
	 * Checks if all dependencies of a DU component exists
	 * @param sleeComponent
	 * @param deployableUnit
	 * @throws DependencyException if a dependency is missing
	 */
	private void checkDependencies(SleeComponent sleeComponent, DeployableUnit deployableUnit) throws DependencyException {
    	for (ComponentID componentID : sleeComponent.getDependenciesSet()) {
    		if (componentID instanceof EventTypeID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((EventTypeID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof LibraryID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((LibraryID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof ProfileSpecificationID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((ProfileSpecificationID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof ResourceAdaptorID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((ResourceAdaptorID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof ResourceAdaptorTypeID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((ResourceAdaptorTypeID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof SbbID) {
    			if (deployableUnit.getDeployableUnitRepository().getComponentByID((SbbID)componentID) == null) {
    				throw new DependencyException("Component "+sleeComponent.getComponentID() + " depends on "+componentID+" which is not available in the component repository or in the deployable unit");
    			}
    		}
    		else if (componentID instanceof ServiceID) {
    			throw new SLEEException("Component "+sleeComponent.getComponentID() + " depends on a service component "+componentID+" which is not available in the component repository or in the deployable unit");
    		}
    	}
    }
    
    /**
	 * Creates the directory that will be used for unpacking the child jars for a given DU.
	 * @param rootDir
	 * @param sourceUrl
	 * @throws SLEEException if the dir can't be created
	 * @return
	 */
    private File createTempDUDeploymentDir(File deploymentRoot, DeployableUnitID deployableUnitID) {
        try {
            // first create a dummy file to gurantee uniqueness. I would have been nice if the File class had a createTempDir() method
            // IVELIN -- do not use jarName here because windows cannot see the path (exceeds system limit)
            File tempFile = File.createTempFile("", "", deploymentRoot);
            File tempDUDeploymentDir = new File(tempFile.getAbsolutePath() + "-mobicents-slee-du");
            if (!tempDUDeploymentDir.exists()) {
            	tempDUDeploymentDir.mkdirs();
            }
            else {
            	throw new SLEEException("Dir "+tempDUDeploymentDir+" already exists, unable to create deployment dir for DU "+deployableUnitID);
            }
            tempFile.delete();
            return tempDUDeploymentDir;
        } catch (IOException e) {            
            throw new SLEEException("Failed to create deployment dir for DU "+deployableUnitID, e);
        }
    }
    
}