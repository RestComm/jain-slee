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

import java.util.List;

import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;

/**
 * @author martins
 *
 */
public interface ResourceAdaptorDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<ResourceAdaptorTypeID> getResourceAdaptorTypeRefs();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileSpecRefDescriptor> getProfileSpecRefs();

	/**
	 * 
	 * @return
	 */
	public List<? extends ConfigPropertyDescriptor> getConfigProperties();

	/**
	 * 
	 * @return
	 */
	public boolean getIgnoreRaTypeEventTypeCheck();

	/**
	 * 
	 * @return
	 */
	public UsageParametersInterfaceDescriptor getResourceAdaptorUsageParametersInterface();

	/**
	 * 
	 * @return
	 */
	public String getResourceAdaptorClassName();

	/**
	 * 
	 * @return
	 */
	public boolean getSupportsActiveReconfiguration();

	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID();

	/**
	 * 
	 * @return
	 */
	public String getSecurityPermissions();

}
