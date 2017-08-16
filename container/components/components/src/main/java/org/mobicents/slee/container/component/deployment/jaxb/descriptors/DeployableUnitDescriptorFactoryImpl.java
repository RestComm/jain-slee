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

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.du.DeployableUnitDescriptorFactory;

/**
 * 
 * @author martins
 *
 */
public class DeployableUnitDescriptorFactoryImpl extends AbstractDescriptorFactory implements DeployableUnitDescriptorFactory {
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.du.DeployableUnitDescriptorFactory#parse(java.io.InputStream)
	 */
	public DeployableUnitDescriptorImpl parse(InputStream inputStream) throws DeploymentException {
		
		Object jaxbPojo = buildJAXBPojo(inputStream);
		
		if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit) {
			return new DeployableUnitDescriptorImpl((org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit)jaxbPojo);
		}
		else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit) {
			return  new DeployableUnitDescriptorImpl((org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit)jaxbPojo);
		} 
		else {
			throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
		}
	}
}
