package org.mobicents.slee.resource.tftp;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.tftp.events.RequestEvent;

/**
 * Caches event types for Tftp Server RA requests
 *
 */
public class EventIDCache {

	private static final String EVENT_PREFIX_REQUEST = "net.java.slee.resource.tftp.events.incoming.request.";
	public static final String VENDOR = "net.java.slee";
	public static final String VERSION = "1.0";

	private ConcurrentHashMap<String, FireableEventType> eventTypes = new ConcurrentHashMap<String, FireableEventType>();

	private final Tracer trc;

	public EventIDCache(Tracer trc) {
		this.trc = trc;
	}

	public static String getEventName(RequestEvent event) {
		return (new StringBuilder(EVENT_PREFIX_REQUEST).append(event.getTypeDescr())).toString();
	}

	public FireableEventType getEventType(EventLookupFacility eventLookupFacility, RequestEvent event) {
		String eventName = getEventName(event);
		FireableEventType eventType = eventTypes.get(eventName); 
		if (eventType == null) {
			try {
				eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, VENDOR, VERSION));
			} catch (Throwable e) {
				trc.severe("Failed to obtain fireable event type for event with name " + eventName, e);
				return null;
			}
			eventTypes.put(eventName, eventType);
		}
		return eventType;	
	}
}