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
