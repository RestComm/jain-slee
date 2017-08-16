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
