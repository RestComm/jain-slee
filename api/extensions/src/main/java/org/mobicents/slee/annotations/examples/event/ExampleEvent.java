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

package org.mobicents.slee.annotations.examples.event;

import java.util.UUID;

import org.mobicents.slee.annotations.EventType;
import org.mobicents.slee.annotations.LibraryRef;

@EventType(name=ExampleEvent.EVENT_TYPE_NAME,
		vendor=ExampleEvent.EVENT_TYPE_VENDOR,
		version=ExampleEvent.EVENT_TYPE_VERSION,
		libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="javax.slee",version="1.0")})
public class ExampleEvent {

	public static final String EVENT_TYPE_NAME = "ExampleEvent";
	public static final String EVENT_TYPE_VENDOR = "javax.slee";
	public static final String EVENT_TYPE_VERSION = "1.0";
	
	private final String id = UUID.randomUUID().toString();

	private final Object eventData;
	
	public ExampleEvent(Object eventData) {
		this.eventData = eventData;
	}
	
	public Object getEventData() {
		return eventData;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExampleEvent other = (ExampleEvent) obj;
		return this.id.equals(other.id);
	}
	
}
