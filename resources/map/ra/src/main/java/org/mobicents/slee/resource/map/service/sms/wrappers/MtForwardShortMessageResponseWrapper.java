package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class MtForwardShortMessageResponseWrapper extends SmsMessageWrapper<MtForwardShortMessageResponse> implements
		MtForwardShortMessageResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_RESPONSE";

	public MtForwardShortMessageResponseWrapper(MAPDialogSmsWrapper mAPDialog, MtForwardShortMessageResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
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
		return "MtForwardShortMessageResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
