package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;

/**
 * @author B. Baranowski
 */
public class RequestEventWrapper extends RequestEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param source
	 * @param serverTx
	 * @param dialog
	 * @param event
	 */
	public RequestEventWrapper(Object source, ServerTransaction serverTx, Dialog dialog, Request event) {
		super(source, serverTx, dialog, event);
	}
	
	@Override	
	public String toString() {
		return new StringBuilder("RequestEventWrapper[ EVENT[").append(getRequest().getMethod())
			.append("] DIALOG[").append(getDialog())
			.append("] TX[").append(getServerTransaction())
			.append("]]").toString();
	}
}
