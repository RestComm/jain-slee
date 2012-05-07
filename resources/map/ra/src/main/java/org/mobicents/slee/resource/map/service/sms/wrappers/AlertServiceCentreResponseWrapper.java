package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class AlertServiceCentreResponseWrapper extends SmsMessageWrapper<AlertServiceCentreResponse> implements
		AlertServiceCentreResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.ALERT_SERVICE_CENTER_RESPONSE";

	public AlertServiceCentreResponseWrapper(MAPDialogSmsWrapper mAPDialog, AlertServiceCentreResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlertServiceCentreResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}
}
