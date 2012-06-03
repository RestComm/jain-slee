package org.mobicents.slee.resource.map.service.supplementary.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryMessage;
import org.mobicents.slee.resource.map.wrappers.MAPMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class SupplementaryMessageWrapper<T extends SupplementaryMessage> extends MAPMessageWrapper<T> implements
		SupplementaryMessage {

	public SupplementaryMessageWrapper(MAPDialogSupplementaryWrapper mapDialogWrapper, String eventTypeName,
			T wrappedEvent) {
		super(mapDialogWrapper, eventTypeName, wrappedEvent);
	}

	public MAPDialogSupplementary getMAPDialog() {
		return (MAPDialogSupplementaryWrapper) this.mapDialogWrapper;
	}

	public byte getUSSDDataCodingScheme() {
		return this.wrappedEvent.getUSSDDataCodingScheme();
	}

	public USSDString getUSSDString() {
		return this.wrappedEvent.getUSSDString();
	}

}
