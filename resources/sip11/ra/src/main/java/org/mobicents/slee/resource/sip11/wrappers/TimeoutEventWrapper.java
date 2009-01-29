package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.Timeout;
import javax.sip.TimeoutEvent;

public class TimeoutEventWrapper extends TimeoutEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8699229560432305798L;
	public TimeoutEventWrapper(Object source, ClientTransaction tx, Timeout timeOut) {
		super(source, tx, timeOut);
		// TODO Auto-generated constructor stub
	}
	public TimeoutEventWrapper(Object source, ServerTransaction tx, Timeout timeOut) {
		super(source, tx, timeOut);
		// TODO Auto-generated constructor stub
	}

}
