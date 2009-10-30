package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

/**
 * 
 *
 */
public class ResponseEventWrapper extends ResponseEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param source
	 * @param clientTx
	 * @param dialog
	 * @param event
	 */
	public ResponseEventWrapper(Object source, ClientTransaction clientTx, Dialog dialog, Response event) {
		super(source, clientTx, dialog, event);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ResponseEventWrapper[ EVENT[").append(getResponse().getStatusCode())
			.append("] DIALOG[").append(getDialog())
			.append("] TX[").append(getClientTransaction())
			.append("]]").toString();
	}
	
}
