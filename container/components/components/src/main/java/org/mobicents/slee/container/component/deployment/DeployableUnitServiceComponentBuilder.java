/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
