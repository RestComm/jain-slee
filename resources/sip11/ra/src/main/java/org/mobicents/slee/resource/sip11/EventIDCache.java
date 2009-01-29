package org.mobicents.slee.resource.sip11;

import java.util.concurrent.ConcurrentHashMap;

import javax.sip.TimeoutEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.EventLookupFacility;

import net.java.slee.resource.sip.DialogForkedEvent;
import net.java.slee.resource.sip.DialogTimeoutEvent;

/**
 * Caches event IDs for the SIP RA.
 * 
 * @author martins
 * 
 */
public class EventIDCache {

	private static final String OUT_OF_DIALOG_REQUEST_EVENT_PREFIX = "javax.sip.message.Request.";
	private static final String RESPONSE_EVENT_PREFIX = "javax.sip.message.Response.";
	private static final String INDIALOG_REQUEST_EVENT_PREFIX = "javax.sip.Dialog.";

	private static final String SIP_EXTENSION_REQUEST_EVENT_NAME_SUFIX = "SIP_EXTENSION";
	
	private static final String VENDOR = "net.java.slee";
	private static final String VERSION = "1.2";

	private static final String TRANSACTION_TIMEOUT_EVENTNAME = "javax.sip.Timeout.TRANSACTION";
	private static final String DIALOG_TIMEOUT_EVENTNAME = "javax.sip.Timeout.Dialog";
	private static final String DIALOG_FORKED_EVENTNAME = "javax.sip.Dialog.FORKED";
	
	private ConcurrentHashMap<String, Integer> eventIds = new ConcurrentHashMap<String, Integer>();

	public EventIDCache() {
	}

	/**
	 * Retrieves the event id for a SIP Request event.
	 * 
	 * @param eventLookupFacility
	 * @param request
	 * @param inDialogActivity
	 *            if the event occurred in a dialog activity or not
	 * @return
	 */
	public int getEventId(EventLookupFacility eventLookupFacility,
			Request request, boolean inDialogActivity) {

		final String requestMethod = request.getMethod();
		
		// Cancel is always the same.
		if (requestMethod.equals(Request.CANCEL))
			inDialogActivity = false;

		int eventID = -1;
		if (inDialogActivity) {
			eventID = getEventId(eventLookupFacility,
					INDIALOG_REQUEST_EVENT_PREFIX + requestMethod);
			if (eventID == -1) {
				eventID = getEventId(eventLookupFacility,
						INDIALOG_REQUEST_EVENT_PREFIX + SIP_EXTENSION_REQUEST_EVENT_NAME_SUFIX);
			}
		} else {
			eventID = getEventId(eventLookupFacility,
					OUT_OF_DIALOG_REQUEST_EVENT_PREFIX + requestMethod);
			if (eventID == -1) {
				eventID = getEventId(eventLookupFacility,
						OUT_OF_DIALOG_REQUEST_EVENT_PREFIX + SIP_EXTENSION_REQUEST_EVENT_NAME_SUFIX);
			}
		}
		return eventID;
	}

	/**
	 * Retrieves the event id for a SIP Response event.
	 * 
	 * @param eventLookupFacility
	 * @param response
	 * @return
	 */
	public int getEventId(EventLookupFacility eventLookupFacility,
			Response response) {

		String statusCodeName = null;
		final int responseStatus = response.getStatusCode();
		if (responseStatus == 100) {
			statusCodeName = "TRYING";
		} else if (100 < responseStatus && responseStatus < 200) {
			statusCodeName = "PROVISIONAL";
		} else if (responseStatus < 300) {
			statusCodeName = "SUCCESS";
		} else if (responseStatus < 400) {
			statusCodeName = "REDIRECT";
		} else if (responseStatus < 500) {
			statusCodeName = "CLIENT_ERROR";
		} else if (responseStatus < 600) {
			statusCodeName = "SERVER_ERROR";
		} else {
			statusCodeName = "GLOBAL_FAILURE";
		}

		// in dialog responses use the 1.1 event id prefix
		return getEventId(eventLookupFacility, RESPONSE_EVENT_PREFIX
				+ statusCodeName);
	}

	/**
	 * Retrieves the event id for a {@link TimeoutEvent} in a SIP Transaction.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public int getTransactionTimeoutEventId(
			EventLookupFacility eventLookupFacility, boolean inDialog) {
		return getEventId(eventLookupFacility, TRANSACTION_TIMEOUT_EVENTNAME);
	}

	/**
	 * Retrieves the event id for a {@link DialogTimeoutEvent}.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public int getDialogTimeoutEventId(EventLookupFacility eventLookupFacility) {
		return getEventId(eventLookupFacility, DIALOG_TIMEOUT_EVENTNAME);
	}

	/**
	 * Retrieves the event id for a {@link DialogForkedEvent}.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public int getDialogForkEventId(EventLookupFacility eventLookupFacility) {
		return getEventId(eventLookupFacility, DIALOG_FORKED_EVENTNAME);
	}

	private int getEventId(EventLookupFacility eventLookupFacility,
			String eventName) {

		Integer integer = eventIds.get(eventName);
		if (integer == null) {
			try {
				integer = Integer.valueOf(eventLookupFacility.getEventID(
						eventName, VENDOR, VERSION));
			} catch (Exception e) {
				integer = Integer.valueOf(-1);
			}
			eventIds.put(eventName, integer);
		}
		return integer.intValue();
	}

}
