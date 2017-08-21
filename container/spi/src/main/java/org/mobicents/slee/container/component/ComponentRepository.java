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

import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;

/**
 * @author martins
 *
 */
public interface ComponentRepository {

	/**
	 * Retrieves the {@link EventTypeComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public EventTypeComponent getComponentByID(EventTypeID id);
	
	/**
	 * Retrieves the {@link LibraryComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public LibraryComponent getComponentByID(LibraryID id);
	
	/**
	 * Retrieves the {@link ProfileSpecificationComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id);
	
	/**
	 * Retrieves the {@link ResourceAdaptorComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id);
	
	/**
	 * Retrieves the {@link ResourceAdaptorTypeComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id);
	
	/**
	 * Retrieves the {@link SbbComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public SbbComponent getComponentByID(SbbID id);

	/**
	 * Retrieves the {@link ServiceComponent} associated with the specified id
	 * @param id
	 * @return null if no such component exists
	 */
	public ServiceComponent getComponentByID(ServiceID id);

	/**
	 * Retrieves the ids for all event components in the repository.
	 * @return
	 */
	public Set<EventTypeID> getEventComponentIDs();
	
	/**
	 * Retrieves the ids for all library components in the repository.
	 * @return
	 */
	public Set<LibraryID> getLibraryIDs();

	/**
	 * Retrieves the ids for all profile specifications components in the repository.
	 * @return
	 */
	public Set<ProfileSpecificationID> getProfileSpecificationIDs();

	/**
	 * @param componentId
	 * @return
	 */
	public ComponentID[] getReferringComponents(ComponentID componentID)
	throws NullPointerException, UnrecognizedComponentException;

	/**
	 * Retrieves the component ids for components that refers the specified component
	 */
	public Set<SleeComponent> getReferringComponents(SleeComponent component);

	/**
	 * Retrieves the ids for all resource adaptor components in the repository.
	 * @return
	 */
	public Set<ResourceAdaptorID> getResourceAdaptorIDs();

	/**
	 * Retrieves the ids for all resource adaptor type components in the repository.
	 * @return
	 */
	public Set<ResourceAdaptorTypeID> getResourceAdaptorTypeIDs();
	
	/**
	 * Retrieves the ids for all sbb components in the repository.
	 * @return
	 */
	public Set<SbbID> getSbbIDs();

	/**
	 * Retrieves the ids for all service components in the repository.
	 * @return
	 */
	public Set<ServiceID> getServiceIDs();
	
	/**
	 * 
	 * @param componentID
	 * @return true if a component with the specified id is in the repository, false otherwise
	 */
	public boolean isInstalled(ComponentID componentID);

	public boolean putComponent(EventTypeComponent component);

	public boolean putComponent(LibraryComponent component);

	public boolean putComponent(ProfileSpecificationComponent component);

	public boolean putComponent(ResourceAdaptorComponent component);

	public boolean putComponent(ResourceAdaptorTypeComponent component);

	public boolean putComponent(SbbComponent component);

	public boolean putComponent(ServiceComponent component);

	public void removeComponent(EventTypeID componentID);

	public void removeComponent(LibraryID componentID);

	public void removeComponent(ProfileSpecificationID componentID);

	public void removeComponent(ResourceAdaptorID componentID);

	public void removeComponent(ResourceAdaptorTypeID componentID);

	public void removeComponent(SbbID componentID);

	public void removeComponent(ServiceID componentID);

}
