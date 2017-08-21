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
package org.mobicents.slee.container.component.sbb;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;


/**
 * @author martins
 *
 */
public interface SbbDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * Retrieves the map between aci attributes names and aliases.
	 * @return
	 */
	public Map<String,String> getActivityContextAttributeAliases();

	public ProfileSpecificationID getAddressProfileSpecRef();

	public Map<String, CMPFieldDescriptor> getCmpFields();

	public Set<EventTypeID> getDefaultEventMask();

	public List<EjbRefDescriptor> getEjbRefs();
	  
	public List<EnvEntryDescriptor> getEnvEntries();
	  
	public Map<EventTypeID, EventEntryDescriptor> getEventEntries();
	  
	/**
	 * Retrieves the map between event names and event types
	 * @return
	 */
	public Map<String,EventTypeID> getEventTypes();

	public Map<String, GetChildRelationMethodDescriptor> getGetChildRelationMethodsMap();

	public Map<String, GetProfileCMPMethodDescriptor> getGetProfileCMPMethods();
	
	public List<ProfileSpecRefDescriptor> getProfileSpecRefs();

	public List<ResourceAdaptorTypeBindingDescriptor> getResourceAdaptorTypeBindings();

	public SbbAbstractClassDescriptor getSbbAbstractClass();

	public String getSbbActivityContextInterface();

	public String getSbbAlias();

	public SbbID getSbbID();

	public SbbLocalInterfaceDescriptor getSbbLocalInterface();
	
	public List<SbbRefDescriptor> getSbbRefs();

	public UsageParametersInterfaceDescriptor getSbbUsageParametersInterface();
	
	public String getSecurityPermissions();

}
