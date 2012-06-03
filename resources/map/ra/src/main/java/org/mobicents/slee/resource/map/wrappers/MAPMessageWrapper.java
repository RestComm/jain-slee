package org.mobicents.slee.resource.map.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * 
 * @author amit bhayani
 *
 */
public abstract class MAPMessageWrapper<T extends MAPMessage> extends MAPEvent<T> implements MAPMessage {

	

	public MAPMessageWrapper(MAPDialogWrapper mapDialogWrapper, String eventTypeName, T wrappedEvent) {
		super(mapDialogWrapper, eventTypeName, wrappedEvent);
	}

	public long getInvokeId() {
		return this.wrappedEvent.getInvokeId();
	}

	public MAPMessageType getMessageType() {
		return this.wrappedEvent.getMessageType();
	}

	public int getOperationCode() {
		return this.wrappedEvent.getOperationCode();
	}

	public void setInvokeId(long arg0) {
		this.wrappedEvent.setInvokeId(arg0);
	}
	
}
