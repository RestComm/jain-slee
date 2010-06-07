/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resources.isup.ra;

import java.util.concurrent.ConcurrentHashMap;
import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

/**
 *
 * @author kulikov
 */
public class FireableEventTypeCache {

    public static final String VENDOR = "net.java";
    public static final String VERSION = "1.0";
    public static final String NAME_PREFIX = "net.java.slee.resource.isup.";
    private ConcurrentHashMap<String, FireableEventType> eventTypes = 
            new ConcurrentHashMap<String, FireableEventType>();
    private final Tracer tracer;

    public FireableEventTypeCache(Tracer tracer) {
        this.tracer = tracer;
    }

    public FireableEventType getEventType(EventLookupFacility eventLookupFacility, String eventName) {
        FireableEventType eventType = eventTypes.get(eventName);
        if (eventType == null) {
            try {
                eventType = eventLookupFacility.getFireableEventType(new EventTypeID(NAME_PREFIX+eventName, VENDOR, VERSION));
            } catch (Throwable e) {
                tracer.severe("Failed to obtain fireable event type for event with name " + eventName, e);
                return null;
            }
            eventTypes.put(eventName, eventType);
        }
        return eventType;
    }
}
