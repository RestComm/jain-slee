package org.mobicents.slee.resource.xmpp;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

/**
 * Caches event types for Xmpp RA
 * @author martins
 *
 */
public class FireableEventTypeCache {

	public static final String VENDOR = "org.jivesoftware.smack";
	public static final String VERSION = "1.0";
		
	private ConcurrentHashMap<String, FireableEventType> eventTypes = new ConcurrentHashMap<String, FireableEventType>();
	
	private final Tracer tracer;
	
	public FireableEventTypeCache(Tracer tracer) {
		this.tracer = tracer;
	}
	
	public String getEventName(Object event) {
		return event.getClass().getName();
	}
	
	public FireableEventType getEventType(EventLookupFacility eventLookupFacility, Object event) {
		String eventName = getEventName(event);
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
