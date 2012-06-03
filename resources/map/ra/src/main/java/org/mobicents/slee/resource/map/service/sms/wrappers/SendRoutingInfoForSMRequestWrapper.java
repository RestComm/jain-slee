package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;

/**
 * 
 * @author amit bhayani
 *
 */
public class SendRoutingInfoForSMRequestWrapper extends SmsMessageWrapper<SendRoutingInfoForSMRequest> implements
		SendRoutingInfoForSMRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_REQUEST";

	public SendRoutingInfoForSMRequestWrapper(MAPDialogSmsWrapper mAPDialog, SendRoutingInfoForSMRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public boolean getGprsSupportIndicator() {
		return this.wrappedEvent.getGprsSupportIndicator();
	}

	public ISDNAddressString getMsisdn() {
		return this.wrappedEvent.getMsisdn();
	}

	public SM_RP_MTI getSM_RP_MTI() {
		return this.wrappedEvent.getSM_RP_MTI();
	}

	public SM_RP_SMEA getSM_RP_SMEA() {
		return this.wrappedEvent.getSM_RP_SMEA();
	}

	public AddressString getServiceCentreAddress() {
		return this.wrappedEvent.getServiceCentreAddress();
	}

	public boolean getSm_RP_PRI() {
		return this.wrappedEvent.getSm_RP_PRI();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SendRoutingInfoForSMRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
