package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.service.sms.SmsMessage;
import org.mobicents.slee.resource.map.wrappers.MAPMessageWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class SmsMessageWrapper<T extends SmsMessage> extends MAPMessageWrapper<T> implements SmsMessage {

	public SmsMessageWrapper(MAPDialogSmsWrapper mapDialogWrapper, String eventTypeName, T wrappedEvent) {
		super(mapDialogWrapper, eventTypeName, wrappedEvent);
	}
	
	public MAPDialogSmsWrapper getMAPDialog() {
		return (MAPDialogSmsWrapper) this.mapDialogWrapper;
	}

}
