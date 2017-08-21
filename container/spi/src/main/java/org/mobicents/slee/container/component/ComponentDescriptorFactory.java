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
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.component.du.DeployableUnitDescriptorFactory;
import org.mobicents.slee.container.component.event.EventTypeDescriptorFactory;
import org.mobicents.slee.container.component.library.LibraryDescriptorFactory;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptorFactory;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptorFactory;
import org.mobicents.slee.container.component.sbb.SbbDescriptorFactory;
import org.mobicents.slee.container.component.service.ServiceDescriptorFactory;

/**
 * @author martins
 *
 */
public interface ComponentDescriptorFactory {

	/**
	 * 
	 * @return
	 */
	public DeployableUnitDescriptorFactory getDeployableUnitDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public EventTypeDescriptorFactory getEventTypeDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public LibraryDescriptorFactory getLibraryDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationDescriptorFactory getProfileSpecificationDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorDescriptorFactory getResourceAdaptorDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorTypeDescriptorFactory getResourceAdaptorTypeDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public SbbDescriptorFactory getSbbDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ServiceDescriptorFactory getServiceDescriptorFactory();

}
