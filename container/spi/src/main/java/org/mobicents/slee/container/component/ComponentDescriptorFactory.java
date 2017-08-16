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
