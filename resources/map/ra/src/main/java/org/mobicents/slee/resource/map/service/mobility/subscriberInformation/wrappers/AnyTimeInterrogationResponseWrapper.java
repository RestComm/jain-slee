package org.mobicents.slee.resource.map.service.mobility.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPDialogMobilityWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MobilityMessageWrapper;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AnyTimeInterrogationResponseWrapper extends MobilityMessageWrapper<AnyTimeInterrogationResponse> implements
		AnyTimeInterrogationResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.mobility.subscriberinfo.ANY_TIME_INTERROGATION_RESPONSE";

	/**
	 * @param mAPDialog
	 */
	public AnyTimeInterrogationResponseWrapper(MAPDialogMobilityWrapper mAPDialog, AnyTimeInterrogationResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

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
		return "AnyTimeInterrogationResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
