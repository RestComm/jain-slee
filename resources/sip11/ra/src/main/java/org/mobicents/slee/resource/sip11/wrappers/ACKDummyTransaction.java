package org.mobicents.slee.resource.sip11.wrappers;

import java.rmi.server.UID;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * 
 *
 */
public class ACKDummyTransaction implements ServerTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final Request ackRequest;

	/**
	 * 
	 */
	private final String branchId;

	/**
	 * 
	 */
	private Object appData = null;

	/**
	 * 
	 * @param ackRequest
	 */
	public ACKDummyTransaction(Request ackRequest) {
		this.ackRequest = ackRequest;
		this.branchId = new UID().toString();
	}

	/*
	 * 
	 */
	public void cleanup() {
		// We dont dare to do here a thing!!!
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.ServerTransaction#enableRetransmissionAlerts()
	 */
	public void enableRetransmissionAlerts() throws SipException {
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getBranchId()
	 */
	public String getBranchId() {
		return branchId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.ServerTransaction#sendResponse(javax.sip.message.Response)
	 */
	public void sendResponse(Response arg0) throws SipException,
			InvalidArgumentException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getApplicationData()
	 */
	public Object getApplicationData() {
		return this.appData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getDialog()
	 */
	public Dialog getDialog() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getRequest()
	 */
	public Request getRequest() {
		return this.ackRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getRetransmitTimer()
	 */
	public int getRetransmitTimer() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#getState()
	 */
	public TransactionState getState() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#setApplicationData(java.lang.Object)
	 */
	public void setApplicationData(Object arg0) {
		this.appData = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#setRetransmitTimer(int)
	 */
	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.Transaction#terminate()
	 */
	public void terminate() throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

}
