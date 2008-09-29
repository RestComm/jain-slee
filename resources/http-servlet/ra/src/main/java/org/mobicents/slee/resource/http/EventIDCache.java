package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.slee.facilities.EventLookupFacility;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

/**
 * Caches event IDs for Http Servlet RA requests
 * @author martins
 *
 */
public class EventIDCache {

	private static final String EVENT_PREFIX_SESSION = "net.java.slee.resource.http.events.incoming.session.";
	private static final String EVENT_PREFIX_REQUEST = "net.java.slee.resource.http.events.incoming.request.";
	public static final String VENDOR = "net.java.slee";
	public static final String VERSION = "1.0";
		
	private ConcurrentHashMap<String, Integer> eventIds = new ConcurrentHashMap<String, Integer>();
	
	public EventIDCache() {
	}
	
	public String getEventName(HttpServletRequestEvent event, HttpSession httpSession) {
		if (httpSession == null) {
			return EVENT_PREFIX_REQUEST + event.getRequest().getMethod();
		} else {
			return EVENT_PREFIX_SESSION + event.getRequest().getMethod();
		}	
	}
	
	public int getEventId(EventLookupFacility eventLookupFacility, HttpServletRequestEvent event, HttpSession httpSession) {
		String eventName = getEventName(event, httpSession);
		Integer integer = eventIds.get(eventName); 
		if (integer == null) {
			try {
				integer = Integer.valueOf(eventLookupFacility.getEventID(eventName, VENDOR, VERSION));
			} catch (Exception e) {
				integer = Integer.valueOf(-1);
			}
			eventIds.put(eventName, integer);
		}
		return integer.intValue();	
	}
	
}
