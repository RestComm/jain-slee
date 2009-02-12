package org.mobicents.slee.container.component.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.DeployableUnit;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Service Component builder
 * @author martins
 *
 */
public class DeployableUnitServiceComponentBuilder {

	private static final Logger logger = Logger.getLogger(DeployableUnitServiceComponentBuilder.class);
	
	/**
	 * Builds a service component contained in the specified du jar file, with the specified and adds it to the specified deployable unit.
	 * 
	 * @param serviceDescriptorFileName
	 * @param deployableUnitJar
	 * @param deployableUnit
	 * @param documentBuilder
	 * @throws DeploymentException
	 */
	public ServiceComponent buildComponent(String serviceDescriptorFileName, JarFile deployableUnitJar, DocumentBuilder documentBuilder) throws DeploymentException {
    	
		ServiceDescriptorImpl descriptor = null;
		// make component jar entry
		JarEntry componentDescriptor = deployableUnitJar.getJarEntry(serviceDescriptorFileName);
		InputStream componentDescriptorInputStream = null;
    	try {
    		deployableUnitJar.getInputStream(componentDescriptor);
    		Document componentDescriptorDocument = documentBuilder.parse(componentDescriptorInputStream);
    		descriptor = new ServiceDescriptorImpl(componentDescriptorDocument);    		
    	} catch (SAXException e) {
    		throw new DeploymentException("failed to parse service descriptor from "+componentDescriptor.getName(),e);
    	} catch (IOException e) {
    		throw new DeploymentException("failed to parse service descriptor from "+componentDescriptor.getName(),e);
    	}
    	finally {
    		if (componentDescriptorInputStream != null) {
    			try {
    				componentDescriptorInputStream.close();
    			} catch (IOException e) {
    				logger.error("failed to close inputstream of descriptor for jar "+componentDescriptor.getName());
    			}
    		}
    	}        
    	
    	if (descriptor != null) {
    		return new ServiceComponent(descriptor);
    	}
    	else {
    		throw new SLEEException("failed to build service component from descriptor "+serviceDescriptorFileName+" in du jar "+deployableUnitJar.getName());
    	}
    }
	
}
