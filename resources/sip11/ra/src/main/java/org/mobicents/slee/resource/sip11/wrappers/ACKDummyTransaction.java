/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
	private final String txId;

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
		this.txId = new UID().toString();
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
		return txId;
	}

	public String getTxId() {
		return txId;
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
		// ignore
	}

}
