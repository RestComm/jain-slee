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

package org.mobicents.slee.resource.diameter.s6a;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;

import org.jdiameter.api.Message;

public class EventIDCache {

    private static final String BASE_PACKAGE_PREFIX = "net.java.slee.resource.diameter.base.events.";
    private static final String S6A_PACKAGE_PREFIX = "net.java.slee.resource.diameter.s6a.events.";
    public static Map<Integer, String> eventNames = new ConcurrentHashMap<Integer, String>();

    static {
        Map<Integer, String> eventsTemp = new HashMap<Integer, String>();

        eventsTemp.put(AuthenticationInformationRequest.COMMAND_CODE, S6A_PACKAGE_PREFIX + "AuthenticationInformation");
        eventsTemp.put(UpdateLocationRequest.COMMAND_CODE, S6A_PACKAGE_PREFIX + "UpdateLocation");
        eventsTemp.put(PurgeUERequest.COMMAND_CODE, S6A_PACKAGE_PREFIX + "PurgeUE");

        eventNames = Collections.unmodifiableMap(eventsTemp);
    }
    public static final String ERROR_ANSWER = BASE_PACKAGE_PREFIX + "ErrorAnswer";
    public static final String EXTENSION_DIAMETER_MESSAGE = BASE_PACKAGE_PREFIX + "ExtensionDiameterMessage";
    private static final String VENDOR = "java.net";
    private static final String VERSION = "0.8";
    private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();

    public EventIDCache() {
    }

    /**
     * 
     * @param eventLookupFacility
     * @param message
     * @return
     */
    public FireableEventType getEventId(EventLookupFacility eventLookupFacility, Message message) {

        FireableEventType eventID = null;

        // Error is always the same.
        if (message.isError()) {
            eventID = getEventId(eventLookupFacility, ERROR_ANSWER);
        } else {
            final int commandCode = message.getCommandCode();
            final boolean isRequest = message.isRequest();

            String eventName = eventNames.get(commandCode);

            if (eventName != null) {
                eventID = getEventId(eventLookupFacility, eventName + (isRequest ? "Request" : "Answer"));
            } else {
                eventID = getEventId(eventLookupFacility, EXTENSION_DIAMETER_MESSAGE);
            }
        }

        return eventID;
    }

    /**
     * 
     * @param eventLookupFacility
     * @param eventName
     * @return
     */
    private FireableEventType getEventId(EventLookupFacility eventLookupFacility, String eventName) {

        FireableEventType eventType = eventIds.get(eventName);
        if (eventType == null) {
            try {
                eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, VENDOR, VERSION));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (eventType != null) {
                eventIds.put(eventName, eventType);
            }
        }
        return eventType;
    }
}
