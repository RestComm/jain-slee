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

import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.event.EventTypeDescriptor;

/**
 * 
 * EventDescriptorImpl.java
 * 
 * <br>
 * Project: mobicents <br>
 * 7:22:39 PM Jan 29, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventTypeDescriptorImpl extends
		AbstractComponentWithLibraryRefsDescriptor implements
		EventTypeDescriptor {

	private final EventTypeID eventTypeID;
	private final String eventClassName;

	public EventTypeDescriptorImpl(MEventDefinition eventDefinition,
			List<LibraryID> libraryRefs, boolean isSlee11)
			throws DeploymentException {
		super(isSlee11);
		super.setLibraryRefs(libraryRefs);
		try {
			this.eventTypeID = new EventTypeID(eventDefinition
					.getEventTypeName(), eventDefinition.getEventTypeVendor(),
					eventDefinition.getEventTypeVersion());
			this.eventClassName = eventDefinition.getEventClassName();

		} catch (Exception e) {
			throw new DeploymentException(
					"failed to build event type descriptor", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.event.EventTypeDescriptor#getEventTypeID
	 * ()
	 */
	public EventTypeID getEventTypeID() {
		return eventTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.event.EventTypeDescriptor#getEventClassName
	 * ()
	 */
	public String getEventClassName() {
		return eventClassName;
	}

}
