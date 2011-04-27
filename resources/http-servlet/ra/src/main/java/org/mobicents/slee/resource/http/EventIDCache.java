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

package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

/**
 * Caches event types for Http Servlet RA requests
 * @author martins
 *
 */
public class EventIDCache {

	private static final String EVENT_PREFIX_SESSION = "net.java.slee.resource.http.events.incoming.session.";
	private static final String EVENT_PREFIX_REQUEST = "net.java.slee.resource.http.events.incoming.request.";
	public static final String VENDOR = "net.java.slee";
	public static final String VERSION = "1.0";
		
	private ConcurrentHashMap<String, FireableEventType> eventTypes = new ConcurrentHashMap<String, FireableEventType>();
	
	private final Tracer tracer;
	
	public EventIDCache(Tracer tracer) {
		this.tracer = tracer;
	}
	
	public String getEventName(HttpServletRequestEvent event, HttpSession httpSession) {
		if (httpSession == null) {
			return new StringBuilder(EVENT_PREFIX_REQUEST).append(event.getRequest().getMethod()).toString();
		} else {
			return new StringBuilder(EVENT_PREFIX_SESSION).append(event.getRequest().getMethod()).toString();
		}	
	}
	
	public FireableEventType getEventType(EventLookupFacility eventLookupFacility, HttpServletRequestEvent event, HttpSession httpSession) {
		String eventName = getEventName(event, httpSession);
		FireableEventType eventType = eventTypes.get(eventName); 
		if (eventType == null) {
			try {
				eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, VENDOR, VERSION));
			} catch (Throwable e) {
				tracer.severe("Failed to obtain fireable event type for event with name "+eventName,e);
				return null;
			}
			eventTypes.put(eventName, eventType);
		}
		return eventType;	
	}
	
}
