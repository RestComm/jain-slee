package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class MtForwardShortMessageRequestWrapper extends SmsMessageWrapper<MtForwardShortMessageRequest> implements
		MtForwardShortMessageRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_REQUEST";

	public MtForwardShortMessageRequestWrapper(MAPDialogSmsWrapper mAPDialog, MtForwardShortMessageRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	@Override
	public boolean getMoreMessagesToSend() {
		return this.wrappedEvent.getMoreMessagesToSend();
	}

	@Override
	public SM_RP_DA getSM_RP_DA() {
		return this.wrappedEvent.getSM_RP_DA();
	}

	@Override
	public SM_RP_OA getSM_RP_OA() {
		return this.wrappedEvent.getSM_RP_OA();
	}

	@Override
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
		return "MtForwardShortMessageRequest [wrapped=" + this.wrappedEvent + "]";
	}

}
