package org.mobicents.slee.resource.lab.ra;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

/**
 * 
 * @author amit bhayani
 *
 */
public class FireableEventTypeCache {
	public static final String VENDOR = "org.mobicents";
	public static final String VERSION = "2.0";

	private ConcurrentHashMap<String, FireableEventType> eventTypes = new ConcurrentHashMap<String, FireableEventType>();

	private final Tracer tracer;

	public FireableEventTypeCache(Tracer tracer) {
		this.tracer = tracer;
	}

	public FireableEventType getEventType(EventLookupFacility eventLookupFacility, String eventName) {
		FireableEventType eventType = eventTypes.get(eventName);
		if (eventType == null) {
			try {
				eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, VENDOR, VERSION));
			} catch (Throwable e) {
				tracer.severe("Failed to obtain fireable event type for event with name " + eventName, e);
				return null;
			}
			eventTypes.put(eventName, eventType);
		}
		return eventType;
	}
}
