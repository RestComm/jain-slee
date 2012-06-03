package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;

/**
 * 
 * @author amit bhayani
 *
 */
public class AlertServiceCentreRequestWrapper extends SmsMessageWrapper<AlertServiceCentreRequest> implements
		AlertServiceCentreRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.ALERT_SERVICE_CENTER_REQUEST";

	public AlertServiceCentreRequestWrapper(MAPDialogSmsWrapper mAPDialog, AlertServiceCentreRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public ISDNAddressString getMsisdn() {
		return this.wrappedEvent.getMsisdn();
	}

	public AddressString getServiceCentreAddress() {
		return this.wrappedEvent.getServiceCentreAddress();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlertServiceCentreRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
