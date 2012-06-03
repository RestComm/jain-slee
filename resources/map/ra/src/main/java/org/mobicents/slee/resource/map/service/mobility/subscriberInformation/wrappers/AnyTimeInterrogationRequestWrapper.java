package org.mobicents.slee.resource.map.service.mobility.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPDialogMobilityWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MobilityMessageWrapper;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AnyTimeInterrogationRequestWrapper extends MobilityMessageWrapper<AnyTimeInterrogationRequest> implements
		AnyTimeInterrogationRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.mobility.subscriberinfo.ANY_TIME_INTERROGATION_REQUEST";

	/**
	 * @param mAPDialog
	 */
	public AnyTimeInterrogationRequestWrapper(MAPDialogMobilityWrapper mAPDialog, AnyTimeInterrogationRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public ISDNAddressString getGsmSCFAddress() {
		return this.wrappedEvent.getGsmSCFAddress();
	}

	public RequestedInfo getRequestedInfo() {
		return this.wrappedEvent.getRequestedInfo();
	}

	public SubscriberIdentity getSubscriberIdentity() {
		return this.wrappedEvent.getSubscriberIdentity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnyTimeInterrogationRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
