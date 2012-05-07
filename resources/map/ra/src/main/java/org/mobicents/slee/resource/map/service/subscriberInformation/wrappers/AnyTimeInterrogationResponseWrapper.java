package org.mobicents.slee.resource.map.service.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class AnyTimeInterrogationResponseWrapper extends
		SubscriberInformationMessageWrapper<AnyTimeInterrogationResponse> implements AnyTimeInterrogationResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.subscriberinfo.ANY_TIME_INTERROGATION_RESPONSE";

	/**
	 * @param mAPDialog
	 */
	public AnyTimeInterrogationResponseWrapper(MAPDialogSubscriberInformationWrapper mAPDialog,
			AnyTimeInterrogationResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	@Override
	public SubscriberInfo getSubscriberInfo() {
		return this.wrappedEvent.getSubscriberInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnyTimeInterrogationResponse [wrapped=" + this.wrappedEvent + "]";
	}

}
