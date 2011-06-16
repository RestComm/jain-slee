/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.sip11;

import java.util.concurrent.ConcurrentHashMap;

import javax.sip.TimeoutEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.FireableEventType;

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
	
	private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();

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
	public FireableEventType getEventId(EventLookupFacility eventLookupFacility,
			Request request, boolean inDialogActivity) {

		final String requestMethod = request.getMethod();
		
		// Cancel is always the same.
		if (requestMethod.equals(Request.CANCEL))
			inDialogActivity = false;

		FireableEventType eventID = null;
		if (inDialogActivity) {
			eventID = getEventId(eventLookupFacility,
					new StringBuilder(INDIALOG_REQUEST_EVENT_PREFIX).append(requestMethod).toString());
			if (eventID == null) {
				eventID = getEventId(eventLookupFacility,
						new StringBuilder(INDIALOG_REQUEST_EVENT_PREFIX).append(SIP_EXTENSION_REQUEST_EVENT_NAME_SUFIX).toString());
			}
		} else {
			eventID = getEventId(eventLookupFacility,
					new StringBuilder(OUT_OF_DIALOG_REQUEST_EVENT_PREFIX).append(requestMethod).toString());
			if (eventID == null) {
				eventID = getEventId(eventLookupFacility,
						new StringBuilder(OUT_OF_DIALOG_REQUEST_EVENT_PREFIX).append(SIP_EXTENSION_REQUEST_EVENT_NAME_SUFIX).toString());
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
	public FireableEventType getEventId(EventLookupFacility eventLookupFacility,
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
		return getEventId(eventLookupFacility, new StringBuilder(RESPONSE_EVENT_PREFIX).append(statusCodeName).toString());
	}

	/**
	 * Retrieves the event id for a {@link TimeoutEvent} in a SIP Transaction.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public FireableEventType getTransactionTimeoutEventId(
			EventLookupFacility eventLookupFacility, boolean inDialog) {
		return getEventId(eventLookupFacility, TRANSACTION_TIMEOUT_EVENTNAME);
	}

	/**
	 * Retrieves the event id for a {@link DialogTimeoutEvent}.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public FireableEventType getDialogTimeoutEventId(EventLookupFacility eventLookupFacility) {
		return getEventId(eventLookupFacility, DIALOG_TIMEOUT_EVENTNAME);
	}

	/**
	 * Retrieves the event id for a {@link DialogForkedEvent}.
	 * 
	 * @param eventLookupFacility
	 * @return
	 */
	public FireableEventType getDialogForkEventId(EventLookupFacility eventLookupFacility) {
		return getEventId(eventLookupFacility, DIALOG_FORKED_EVENTNAME);
	}

	private FireableEventType getEventId(EventLookupFacility eventLookupFacility,
			String eventName) {

		FireableEventType eventType = eventIds.get(eventName);
		if (eventType == null) {
			try {
				eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName,VENDOR,VERSION));
			} catch (UnrecognizedEventException uee) {
				// simply not found, so return null.
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
