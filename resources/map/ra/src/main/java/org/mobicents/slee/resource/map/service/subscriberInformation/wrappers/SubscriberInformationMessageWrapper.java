package org.mobicents.slee.resource.map.service.subscriberInformation.wrappers;

import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPDialogSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberInformationMessage;
import org.mobicents.slee.resource.map.wrappers.MAPMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class SubscriberInformationMessageWrapper<T extends SubscriberInformationMessage> extends MAPMessageWrapper<T>
		implements SubscriberInformationMessage {

	public SubscriberInformationMessageWrapper(MAPDialogSubscriberInformationWrapper mapDialogWrapper,
			String eventTypeName, T wrappedEvent) {
		super(mapDialogWrapper, eventTypeName, wrappedEvent);
	}

	public MAPDialogSubscriberInformation getMAPDialog() {
		return (MAPDialogSubscriberInformationWrapper) this.mapDialogWrapper;
	}

}
