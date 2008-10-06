package org.mobicents.slee.resource.sip11;

import java.util.concurrent.ConcurrentHashMap;

import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.EventLookupFacility;

public class EventIDCache {

	private static final String EVENT_PREFIX_1_2 = "javax.sip.dialog.";
	private static final String EVENT_PREFIX_1_1 = "javax.sip.message.";
	private static final String VENDOR_1_2 = "net.java";
	private static final String VERSION_1_2 = "1.2";
	private static final String VENDOR_1_1 = "javax.sip";
	private static final String VERSION_1_1 = "1.1";
	private static final String RESPONSE_MIDIX = "Response.";
	private static final String REQUEST_MIDIX = "Request.";
	private static final String TIMEOUT_EVENTNAME = "javax.sip.timeout.TRANSACTION";
	private ConcurrentHashMap<String, Integer> eventIds = new ConcurrentHashMap<String, Integer>();
	
	public EventIDCache() {
	}
	
	public int getEventId(EventLookupFacility eventLookupFacility, Request request, boolean inDialog) {
		if (inDialog) {
			return getEventId(eventLookupFacility, EVENT_PREFIX_1_2 + REQUEST_MIDIX + request.getMethod(), VENDOR_1_2, VERSION_1_2);
		} else {
			return getEventId(eventLookupFacility, EVENT_PREFIX_1_1 + REQUEST_MIDIX + request.getMethod(), VENDOR_1_1, VERSION_1_1);
		}	
	}
	
	public int getEventId(EventLookupFacility eventLookupFacility, Response response, boolean inDialog) {
		
		int statusCode = response.getStatusCode();
		String statusCodeName = null;
		if (statusCode == 100) {
			statusCodeName = "TRYING";
		} else if (100 < statusCode && statusCode < 200) {
			statusCodeName = "INFORMATIONAL";
		} else if (statusCode < 300) {
			statusCodeName = "SUCCESS";
		} else if (statusCode < 400) {
			statusCodeName = "REDIRECT";
		} else if (statusCode < 500) {
			statusCodeName = "CLIENT_ERROR";
		} else if (statusCode < 600) {
			statusCodeName = "SERVER_ERROR";
		} else {
			statusCodeName = "GLOBAL_FAILURE";
		}
		
		if (inDialog) {
			// in dialog responses use the 1.1 event id prefix
			return getEventId(eventLookupFacility, EVENT_PREFIX_1_1 + RESPONSE_MIDIX + statusCodeName, VENDOR_1_2, VERSION_1_2);
		} else {
			return getEventId(eventLookupFacility, EVENT_PREFIX_1_1 + RESPONSE_MIDIX + statusCodeName, VENDOR_1_1, VERSION_1_1);
		}
	}
	
	public int getTimeoutEventId(EventLookupFacility eventLookupFacility, boolean inDialog) {
		if (inDialog) {
			return getEventId(eventLookupFacility, TIMEOUT_EVENTNAME, VENDOR_1_2, VERSION_1_2);
		} else {
			return getEventId(eventLookupFacility, TIMEOUT_EVENTNAME, VENDOR_1_1, VERSION_1_1);
		}	
	}
	
	private int getEventId(EventLookupFacility eventLookupFacility, String eventName, String eventVendor, String eventVersion) {
		String key = eventName+eventVersion;
		Integer integer = eventIds.get(key); 
		if (integer == null) {
			try {
				integer = Integer.valueOf(eventLookupFacility.getEventID(eventName, eventVendor, eventVersion));
			} catch (Exception e) {
				integer = Integer.valueOf(-1);
			}
			eventIds.put(key, integer);
		}
		return integer.intValue();
	}
	
}
