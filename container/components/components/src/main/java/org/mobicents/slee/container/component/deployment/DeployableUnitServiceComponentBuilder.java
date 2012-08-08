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

package org.mobicents.slee.container.component.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.ServiceComponentImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;

/**
 * Service Component builder
 * @author martins
 *
 */
public class DeployableUnitServiceComponentBuilder {

	private static final Logger logger = Logger.getLogger(DeployableUnitServiceComponentBuilder.class);
	
	private final ComponentManagementImpl componentManagement;
	
	/**
	 * 
	 */
	public DeployableUnitServiceComponentBuilder(ComponentManagementImpl componentManagement) {
		this.componentManagement = componentManagement;
	}
	
	/**
	 * Builds a service component contained in the specified du jar file, with the specified and adds it to the specified deployable unit.
	 * 
	 * @param serviceDescriptorFileName
	 * @param deployableUnitJar
	 * @param deployableUnit
	 * @param documentBuilder
	 * @throws DeploymentException
	 */
	public List<ServiceComponentImpl> buildComponents(String serviceDescriptorFileName, JarFile deployableUnitJar) throws DeploymentException {
    	
		// make component jar entry
		JarEntry componentDescriptor = deployableUnitJar.getJarEntry(serviceDescriptorFileName);
		InputStream componentDescriptorInputStream = null;
		List<ServiceComponentImpl> result = new ArrayList<ServiceComponentImpl>();
    	try {
    		componentDescriptorInputStream = deployableUnitJar.getInputStream(componentDescriptor);
    		ServiceDescriptorFactoryImpl descriptorFactory = componentManagement.getComponentDescriptorFactory().getServiceDescriptorFactory();
    		for (ServiceDescriptorImpl descriptor : descriptorFactory.parse(componentDescriptorInputStream)) {
    			result.add(new ServiceComponentImpl(descriptor));
    		}
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
    	return result;
    }
	
}
