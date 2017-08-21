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
