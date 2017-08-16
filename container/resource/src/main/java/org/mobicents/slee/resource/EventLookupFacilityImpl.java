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

package org.mobicents.slee.resource;

import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.FireableEventType;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * Implementation of the SLEE 1.1 specs {@link EventLookupFacility} class.
 * 
 * @author martins
 * 
 */
public class EventLookupFacilityImpl implements EventLookupFacility {

	/**
	 * the container
	 */
	private final SleeContainer container;

	/**
	 * the ra entity
	 */
	private final ResourceAdaptorEntity raEntity;

	public EventLookupFacilityImpl(ResourceAdaptorEntity raEntity,SleeContainer container
			) {
		this.container = container;
		this.raEntity = raEntity;
	}

	public FireableEventType getFireableEventType(EventTypeID eventTypeID)
			throws NullPointerException, UnrecognizedEventException,
			FacilityException {

		if (eventTypeID == null) {
			throw new NullPointerException("null EventTypeID");
		}
		EventTypeComponent eventTypeComponent = container
				.getComponentRepository().getComponentByID(eventTypeID);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException(eventTypeID.toString());
		}
		final Set<EventTypeID> allowedEventTypes = raEntity.getAllowedEventTypes();
		if (allowedEventTypes != null
				&& !allowedEventTypes.contains(eventTypeID)) {
			/*
			 * An UnrecognizedEventException is also thrown if the eventType
			 * argument does not identify an event type that the Resource
			 * Adaptor may fire (as determined by the resource adaptor types
			 * referenced by the Resource Adaptor) unless event type checking
			 * has been disabled for the Resource Adaptor (see Section 15.10)
			 */
			throw new UnrecognizedEventException("ra not allowed to lookup "
					+ eventTypeID);
		}
		return new FireableEventTypeImpl(eventTypeComponent.getClassLoader(),
				eventTypeComponent.getDescriptor().getEventClassName(),
				eventTypeComponent.getEventTypeID());
	}

}
