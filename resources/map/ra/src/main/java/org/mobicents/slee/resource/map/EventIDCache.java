package org.mobicents.slee.resource.map;

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
public class EventIDCache {
	
	private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();
	private static final String EVENT_VENDOR = "org.mobicents";
	private static final String EVENT_VERSION = "1.0";

	private Tracer tracer;
	
	protected EventIDCache(Tracer tracer){
		this.tracer = tracer;
	}
	
	protected FireableEventType getEventId(
			EventLookupFacility eventLookupFacility, String eventName) {

		FireableEventType eventType = eventIds.get(eventName);
		if (eventType == null) {
			try {
				eventType = eventLookupFacility
						.getFireableEventType(new EventTypeID(eventName,
								EVENT_VENDOR, EVENT_VERSION));
			} catch (Throwable e) {
				this.tracer.severe("Error while looking up for MAP Events", e);
			}
			if (eventType != null) {
				eventIds.put(eventName, eventType);
			}
		}
		return eventType;
	}
}
