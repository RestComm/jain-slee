package net.java.slee.resource.sip;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;

/**
 * The event that signals the arrival of a CANCEL SIP Request.
 * 
 */
public class CancelRequestEvent extends RequestEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the {@link ServerTransaction} that matches this event
	 */
	private final ServerTransaction matchingTransaction;

	public CancelRequestEvent(Object source,
			ServerTransaction serverTransaction,
			ServerTransaction matchingTransaction, Dialog dialog,
			Request request) {
		super(source, serverTransaction, dialog, request);
		this.matchingTransaction = matchingTransaction;
	}

	/**
	 * Retrieves the {@link ServerTransaction} that matches this event.
	 * 
	 * @return null if there is no matching transaction.
	 */
	public ServerTransaction getMatchingTransaction() {
		return this.matchingTransaction;
	}

}
