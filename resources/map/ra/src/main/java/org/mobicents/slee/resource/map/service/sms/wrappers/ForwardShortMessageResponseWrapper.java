package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class ForwardShortMessageResponseWrapper extends SmsMessageWrapper<ForwardShortMessageResponse> implements
		ForwardShortMessageResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.FORWARD_SHORT_MESSAGE_RESPONSE";

	public ForwardShortMessageResponseWrapper(MAPDialogSmsWrapper mAPDialog, ForwardShortMessageResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForwardShortMessageResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}
}
