package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class ForwardShortMessageRequestWrapper extends SmsMessageWrapper<ForwardShortMessageRequest> implements
		ForwardShortMessageRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.FORWARD_SHORT_MESSAGE_REQUEST";

	public ForwardShortMessageRequestWrapper(MAPDialogSmsWrapper mAPDialog, ForwardShortMessageRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public boolean getMoreMessagesToSend() {
		return this.wrappedEvent.getMoreMessagesToSend();
	}

	public SM_RP_DA getSM_RP_DA() {
		return this.wrappedEvent.getSM_RP_DA();
	}

	public SM_RP_OA getSM_RP_OA() {
		return this.wrappedEvent.getSM_RP_OA();
	}

	public SmsSignalInfo getSM_RP_UI() {
		return this.wrappedEvent.getSM_RP_UI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ForwardShortMessageRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
