package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;

/**
 * 
 * @author amit bhayani
 *
 */
public class InformServiceCentreRequestWrapper extends SmsMessageWrapper<InformServiceCentreRequest> implements
		InformServiceCentreRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.INFORM_SERVICE_CENTER_REQUEST";

	public InformServiceCentreRequestWrapper(MAPDialogSmsWrapper mAPDialog, InformServiceCentreRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAbsentSubscriberDiagnosticSM();
	}

	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAdditionalAbsentSubscriberDiagnosticSM();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public MWStatus getMwStatus() {
		return this.wrappedEvent.getMwStatus();
	}

	public ISDNAddressString getStoredMSISDN() {
		return this.wrappedEvent.getStoredMSISDN();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InformServiceCentreRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
