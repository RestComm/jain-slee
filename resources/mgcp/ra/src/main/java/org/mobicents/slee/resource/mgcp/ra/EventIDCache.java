package org.mobicents.slee.resource.mgcp.ra;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class EventIDCache {

	private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();
	private static final String EVENT_VENDOR = "net.java";
	private static final String EVENT_VERSION = "1.0";

	protected FireableEventType getEventId(
			EventLookupFacility eventLookupFacility, String eventName) {

		FireableEventType eventType = eventIds.get(eventName);
		if (eventType == null) {
			try {
				eventType = eventLookupFacility
						.getFireableEventType(new EventTypeID(eventName,
								EVENT_VENDOR, EVENT_VERSION));
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
