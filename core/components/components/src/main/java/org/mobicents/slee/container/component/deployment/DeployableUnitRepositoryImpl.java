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

package org.mobicents.slee.container.component.deployment;

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

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.du.DeployableUnitRepository;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;


/**
 * Extends the component repository, providing access also to the components in a DeployableUnit, those components are not installed yet
 * @author martins
 *
 */
public class DeployableUnitRepositoryImpl implements DeployableUnitRepository {

	private final DeployableUnitImpl deployableUnit;
	private final ComponentRepository componentRepository;
	
	public DeployableUnitRepositoryImpl(DeployableUnitImpl deployableUnit,ComponentRepository componentRepository) {
		if (deployableUnit == null) {
			throw new NullPointerException("null deployableUnit");
		}
		if (componentRepository == null) {
			throw new NullPointerException("null componentRepository");
		}
		this.deployableUnit = deployableUnit;
		this.componentRepository = componentRepository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.EventTypeID)
	 */
	public EventTypeComponent getComponentByID(EventTypeID id) {
		// get from repository
		EventTypeComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getEventTypeComponents().get(id);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.profile.ProfileSpecificationID)
	 */
	public ProfileSpecificationComponent getComponentByID(
			ProfileSpecificationID id) {
		// get from repository
		ProfileSpecificationComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getProfileSpecificationComponents().get(id);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.management.LibraryID)
	 */
	public LibraryComponent getComponentByID(LibraryID id) {
		// get from repository
		LibraryComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getLibraryComponents().get(id);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.resource.ResourceAdaptorID)
	 */
	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
		// get from repository
		ResourceAdaptorComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getResourceAdaptorComponents().get(id);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.resource.ResourceAdaptorTypeID)
	 */
	public ResourceAdaptorTypeComponent getComponentByID(
			ResourceAdaptorTypeID id) {
		// get from repository
		ResourceAdaptorTypeComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getResourceAdaptorTypeComponents().get(id);
		}
		return component;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.SbbID)
	 */
	public SbbComponent getComponentByID(SbbID id) {
		// get from repository
		SbbComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getSbbComponents().get(id);
		}
		return component;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getComponentByID(javax.slee.ServiceID)
	 */
	public ServiceComponent getComponentByID(ServiceID id) {
		// get from repository
		ServiceComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getServiceComponents().get(id);
		}
		return component;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#isInstalled(javax.slee.ComponentID)
	 */
	public boolean isInstalled(ComponentID componentID) {
		return componentRepository.isInstalled(componentID);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getEventComponentIDs()
	 */
	public Set<EventTypeID> getEventComponentIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getLibraryIDs()
	 */
	public Set<LibraryID> getLibraryIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getProfileSpecificationIDs()
	 */
	public Set<ProfileSpecificationID> getProfileSpecificationIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(org.mobicents.slee.core.component.SleeComponent)
	 */
	public Set<SleeComponent> getReferringComponents(
			SleeComponent component) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorIDs()
	 */
	public Set<ResourceAdaptorID> getResourceAdaptorIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorTypeIDs()
	 */
	public Set<ResourceAdaptorTypeID> getResourceAdaptorTypeIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getSbbIDs()
	 */
	public Set<SbbID> getSbbIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getServiceIDs()
	 */
	public Set<ServiceID> getServiceIDs() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(javax.slee.ComponentID)
	 */
	public ComponentID[] getReferringComponents(ComponentID componentID)
			throws NullPointerException, UnrecognizedComponentException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.event.EventTypeComponent)
	 */
	public boolean putComponent(EventTypeComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.library.LibraryComponent)
	 */
	public boolean putComponent(LibraryComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public boolean putComponent(ProfileSpecificationComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ra.ResourceAdaptorComponent)
	 */
	public boolean putComponent(ResourceAdaptorComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeComponent)
	 */
	public boolean putComponent(ResourceAdaptorTypeComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.sbb.SbbComponent)
	 */
	public boolean putComponent(SbbComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public boolean putComponent(ServiceComponent component) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.EventTypeID)
	 */
	public void removeComponent(EventTypeID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.management.LibraryID)
	 */
	public void removeComponent(LibraryID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.profile.ProfileSpecificationID)
	 */
	public void removeComponent(ProfileSpecificationID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorID)
	 */
	public void removeComponent(ResourceAdaptorID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorTypeID)
	 */
	public void removeComponent(ResourceAdaptorTypeID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.SbbID)
	 */
	public void removeComponent(SbbID componentID) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.ServiceID)
	 */
	public void removeComponent(ServiceID componentID) {
		throw new UnsupportedOperationException();
	}
	
}
