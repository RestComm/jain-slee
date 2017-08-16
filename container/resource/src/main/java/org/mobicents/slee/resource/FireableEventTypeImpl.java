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

import javax.slee.EventTypeID;
import javax.slee.resource.FireableEventType;

/**
 * Implementation of the SLEE 1.1 specs {@link FireableEventType} class.
 * @author martins
 *
 */
public class FireableEventTypeImpl implements FireableEventType {

	/**
	 * the event's class loader, which can be used to load the class returned by {@link FireableEventType#getEventClassName()}
	 */
	private final ClassLoader eventClassLoader;
	
	/**
	 * the event's class name
	 */
	private final String eventClassName;
	
	/**
	 * the event type ID
	 */
	private final EventTypeID eventTypeID;
	
	
	public FireableEventTypeImpl(ClassLoader eventClassLoader,
			String eventClassName, EventTypeID eventTypeID) {
		this.eventClassLoader = eventClassLoader;
		this.eventClassName = eventClassName;
		this.eventTypeID = eventTypeID;
	}

	public ClassLoader getEventClassLoader() {
		return eventClassLoader;
	}

	public String getEventClassName() {
		return eventClassName;
	}

	public EventTypeID getEventType() {
		return eventTypeID;
	}

	@Override
	public int hashCode() {
		return eventTypeID.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((FireableEventTypeImpl)obj).eventTypeID.equals(this.eventTypeID);
		}
		else {
			return false;
		}
	}
}
