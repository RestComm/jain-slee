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
