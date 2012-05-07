package org.mobicents.slee.resource.map.service.lsm.wrappers;

import org.mobicents.protocols.ss7.map.api.service.lsm.LsmMessage;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.slee.resource.map.wrappers.MAPMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class LsmMessageWrapper<T extends LsmMessage> extends MAPMessageWrapper<T> implements LsmMessage {

	public LsmMessageWrapper(MAPDialogLsmWrapper mapDialogWrapper, String eventTypeName, T wrappedEvent) {
		super(mapDialogWrapper, eventTypeName, wrappedEvent);
	}

	public MAPDialogLsm getMAPDialog() {
		return (MAPDialogLsmWrapper) this.mapDialogWrapper;
	}

}
