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
 * Project: restcomm <br>
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
