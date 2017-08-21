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
