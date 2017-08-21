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
