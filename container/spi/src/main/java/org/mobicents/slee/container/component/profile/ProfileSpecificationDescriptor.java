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
package org.mobicents.slee.container.component.profile;

import java.util.List;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.profile.cmp.ProfileCMPInterfaceDescriptor;
import org.mobicents.slee.container.component.profile.query.CollatorDescriptor;
import org.mobicents.slee.container.component.profile.query.QueryDescriptor;

/**
 * @author martins
 * 
 */
public interface ProfileSpecificationDescriptor extends
		ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<? extends CollatorDescriptor> getCollators();

	/**
	 * 
	 * @return
	 */
	public List<? extends EnvEntryDescriptor> getEnvEntries();

	/**
	 * 
	 * @return
	 */
	public boolean getEventsEnabled();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileIndexDescriptor> getIndexedAttributes();

	/**
	 * 
	 * @return
	 */
	public ProfileAbstractClassDescriptor getProfileAbstractClass();

	/**
	 * 
	 * @return
	 */
	public ProfileCMPInterfaceDescriptor getProfileCMPInterface();

	/**
	 * 
	 * @return
	 */
	public ProfileLocalInterfaceDescriptor getProfileLocalInterface();

	/**
	 * 
	 * @return
	 */
	public String getProfileManagementInterface();

	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationID getProfileSpecificationID();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileSpecRefDescriptor> getProfileSpecRefs();

	/**
	 * 
	 * @return
	 */
	public String getProfileTableInterface();

	/**
	 * 
	 * @return
	 */
	public UsageParametersInterfaceDescriptor getProfileUsageParameterInterface();

	/**
	 * 
	 * @return
	 */
	public List<? extends QueryDescriptor> getQueryElements();

	/**
	 * 
	 * @return
	 */
	public boolean getReadOnly();

	/**
	 * 
	 * @return
	 */
	public String getSecurityPermissions();

	/**
	 * 
	 * @return
	 */
	public boolean isIsolateSecurityPermissions();

	/**
	 * 
	 * @return
	 */
	public boolean isSingleProfile();

}
