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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.ArrayList;
import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype.MResourceAdaptorTypeClasses;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptor;

/**
 * 
 * ResourceAdaptorTypeDescriptorImpl.java
 * 
 * <br>
 * Project: mobicents <br>
 * 5:24:59 PM Jan 23, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ResourceAdaptorTypeDescriptorImpl extends
		AbstractComponentWithLibraryRefsDescriptor implements
		ResourceAdaptorTypeDescriptor {

	private ResourceAdaptorTypeID resourceAdaptorTypeID;

	private List<EventTypeID> eventTypeRefs;
	private List<String> activityTypes;

	private String activityContextInterfaceFactoryInterface;
	private String resourceAdaptorInterface;

	/**
	 * Constructor for JAIN SLEE RA Type
	 * 
	 * @param resourceAdaptorType
	 * @param isSlee11
	 */
	public ResourceAdaptorTypeDescriptorImpl(
			MResourceAdaptorType resourceAdaptorType, boolean isSlee11)
			throws DeploymentException {

		super(isSlee11);

		try {
			this.resourceAdaptorTypeID = new ResourceAdaptorTypeID(
					resourceAdaptorType.getResourceAdaptorTypeName(),
					resourceAdaptorType.getResourceAdaptorTypeVendor(),
					resourceAdaptorType.getResourceAdaptorTypeVersion());

			super.setLibraryRefs(resourceAdaptorType.getLibraryRefs());

			this.eventTypeRefs = new ArrayList<EventTypeID>();
			for (EventTypeID eventTypeRef : resourceAdaptorType.getEventTypeRefs()) {
				this.eventTypeRefs.add(eventTypeRef);
			}
			super.dependenciesSet.addAll(eventTypeRefs);

			final MResourceAdaptorTypeClasses resourceAdaptorTypeClasses = resourceAdaptorType
					.getResourceAdaptorTypeClasses();

			this.activityTypes = resourceAdaptorTypeClasses.getActivityType();

			this.activityContextInterfaceFactoryInterface = resourceAdaptorTypeClasses
					.getActivityContextInterfaceFactoryInterface() != null ? resourceAdaptorTypeClasses
					.getActivityContextInterfaceFactoryInterface()
					.getActivityContextInterfaceFactoryInterfaceName()
					: null;
			this.resourceAdaptorInterface = resourceAdaptorTypeClasses
					.getResourceAdaptorInterface() != null ? resourceAdaptorTypeClasses
					.getResourceAdaptorInterface()
					.getResourceAdaptorInterfaceName()
					: null;

		} catch (Exception e) {
			throw new DeploymentException(
					"Failed to build Resource Adaptot Type descriptor", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptor
	 * #getEventTypeRefs()
	 */
	public List<EventTypeID> getEventTypeRefs() {
		return eventTypeRefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptor
	 * #getActivityTypes()
	 */
	public List<String> getActivityTypes() {
		return activityTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptor
	 * #getActivityContextInterfaceFactoryInterface()
	 */
	public String getActivityContextInterfaceFactoryInterface() {
		return activityContextInterfaceFactoryInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptor
	 * #getResourceAdaptorInterface()
	 */
	public String getResourceAdaptorInterface() {
		return resourceAdaptorInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeDescriptor
	 * #getResourceAdaptorTypeID()
	 */
	public ResourceAdaptorTypeID getResourceAdaptorTypeID() {
		return resourceAdaptorTypeID;
	}

}
