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

/**
 * 
 */
package org.mobicents.slee.container.component.ra;

import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;

/**
 * 
 * @author martins
 * 
 */
public interface ResourceAdaptorComponent extends
		SleeComponentWithUsageParametersInterface {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public ResourceAdaptorDescriptor getDescriptor();
	
	/**
	 * Creates an instance of the {@link ConfigProperties} for this component
	 * 
	 * @return
	 */
	public ConfigProperties getDefaultConfigPropertiesInstance();

	/**
	 * Retrieves the ra class
	 * 
	 * @return
	 */
	public Class<?> getResourceAdaptorClass();

	/**
	 * Retrieves the ra id
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.resource.ResourceAdaptorDescriptor getSpecsDescriptor();

	/**
	 * Sets the ra class
	 * 
	 * @param c
	 */
	public void setResourceAdaptorClass(Class<?> c);

}
