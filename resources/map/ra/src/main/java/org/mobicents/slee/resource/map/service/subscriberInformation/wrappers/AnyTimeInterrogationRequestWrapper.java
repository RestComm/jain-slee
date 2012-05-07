package org.mobicents.slee.resource.map.service.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.RequestedInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class AnyTimeInterrogationRequestWrapper extends
		SubscriberInformationMessageWrapper<AnyTimeInterrogationRequest> implements AnyTimeInterrogationRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.subscriberinfo.ANY_TIME_INTERROGATION_REQUEST";

	/**
	 * @param mAPDialog
	 */
	public AnyTimeInterrogationRequestWrapper(MAPDialogSubscriberInformationWrapper mAPDialog,
			AnyTimeInterrogationRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	@Override
	public ISDNAddressString getGsmSCFAddress() {
		return this.wrappedEvent.getGsmSCFAddress();
	}

	@Override
	public RequestedInfo getRequestedInfo() {
		return this.wrappedEvent.getRequestedInfo();
	}

	@Override
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
		return "AnyTimeInterrogationRequest [wrapped=" + this.wrappedEvent + "]";
	}

}
