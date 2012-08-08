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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MService;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MServiceXML;
import org.mobicents.slee.container.component.service.ServiceDescriptorFactory;

/**
 * Factory to build {@link ServiceDescriptorImpl} objects.
 * @author martins
 *
 */
public class ServiceDescriptorFactoryImpl extends AbstractDescriptorFactory implements ServiceDescriptorFactory {
	
	/**
	 * Builds a list of {@link ServiceDescriptorImpl} objects, from an {@link InputStream} containing the event jar xml.
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<ServiceDescriptorImpl> parse(InputStream inputStream) throws DeploymentException {
		
		Object jaxbPojo = buildJAXBPojo(inputStream);
		
		List<ServiceDescriptorImpl> result = new ArrayList<ServiceDescriptorImpl>();
		
		MServiceXML serviceXML = null;
		if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml) {
			serviceXML = new MServiceXML((org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml)jaxbPojo);
		}
		else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml) {
			serviceXML = new MServiceXML((org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml)jaxbPojo);
		} 
		else {
			throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
		}
		
		for (MService mService : serviceXML.getMServices()) {
			result.add(new ServiceDescriptorImpl(mService));
		}
		return result;
	}
}
