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

package org.mobicents.slee.resource.sip11.test.eventidcache;

import java.util.HashMap;
import java.util.Map;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.FireableEventType;

public class EventLookupFacility implements javax.slee.facilities.EventLookupFacility {
	
	private Map<EventTypeID,FireableEventType> eventIds = new HashMap<EventTypeID, FireableEventType>();
	
	public FireableEventType getFireableEventType(EventTypeID arg0)
			throws NullPointerException, UnrecognizedEventException,
			FacilityException {
		return eventIds.get(arg0);
	}

	public void putEventID(String name, String vendor, String version,
			FireableEventType eventType) {
		eventIds.put(new EventTypeID(name,vendor,version), eventType);
	}

}
