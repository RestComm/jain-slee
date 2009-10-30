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
