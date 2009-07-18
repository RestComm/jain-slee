package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.message.SIPRequest;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class ACKDummyTransaction implements ServerTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Request ackRequest = null;
	private Object appData=null;
	private String branchId = null;
	
	public ACKDummyTransaction(Request ackRequest) {
		this.ackRequest = ackRequest;
	}

	public void cleanup() {

		// We dont dare to do here a thing!!!
	}

	public void enableRetransmissionAlerts() throws SipException {
		throw new UnsupportedOperationException();

	}

	public String getBranchId() {
		if (branchId == null) {
			branchId = ((SIPRequest)ackRequest).getTopmostVia().getBranch();
		}
		return branchId;
	}

	public void sendResponse(Response arg0) throws SipException, InvalidArgumentException {
		throw new UnsupportedOperationException();
		
	}

	public Object getApplicationData() {
		
		return this.appData;
	}

	public Dialog getDialog() {

		return null;
	}

	public Request getRequest() {
		return this.ackRequest;
	}

	public int getRetransmitTimer() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public TransactionState getState() {
		
		return null;
	}

	public void setApplicationData(Object arg0) {
		this.appData=arg0;
	}

	public void setRetransmitTimer(int arg0) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}

	public void terminate() throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

	

}
