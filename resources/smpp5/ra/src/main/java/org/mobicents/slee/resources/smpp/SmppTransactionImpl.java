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

package org.mobicents.slee.resources.smpp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.slee.facilities.Tracer;

import net.java.slee.resources.smpp.SmppSession;
import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.SmppError;
import net.java.slee.resources.smpp.pdu.SmppRequest;

import org.mobicents.slee.resources.smpp.pdu.GenericNackImpl;
import org.mobicents.slee.resources.smpp.pdu.SmppErrorImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SmppTransactionImpl implements SmppTransaction {

	private static Tracer tracer;

	private SmppResourceAdaptor smppRA;
	private SmppSessionImpl smppSess;

	private long sequenceNumber;
	private SmppRequest requestPDU;

	private ResponseNotSent responseNotSent;
	private ResponseNotReceived responseNotReceived;

	private final SmppTransactionHandle handle;
	
	protected SmppTransactionImpl(SmppTransactionHandle handle,SmppRequest requestPDU, SmppResourceAdaptor smppRA, SmppSessionImpl smppSess) {
		this.requestPDU = requestPDU;
		this.smppRA = smppRA;
		if (tracer == null) {
			tracer = this.smppRA.getRAContext().getTracer(SmppTransactionImpl.class.getSimpleName());
		}
		this.smppSess = smppSess;
		this.sequenceNumber = this.requestPDU.getSequenceNum();
		this.handle = handle;
	}

	public SmppTransactionHandle getHandle() {
		return handle;
	}
	
	public long getId() {
		return this.sequenceNumber;
	}

	public SmppSession getSmppSession() {
		return smppSess;
	}

	protected SmppRequest getSmppRequest() {
		return this.requestPDU;
	}

	/**
	 * Reset the Timers
	 */
	protected void setResponseNotSentTimeout() {
		responseNotSent = new ResponseNotSent(this);
		this.smppSess.timer.schedule(responseNotSent, this.smppRA.getSmppResponseSentTimeout(), TimeUnit.MILLISECONDS);
	}

	protected void setResponseNotReceivedTimeout() {
		responseNotReceived = new ResponseNotReceived(this);
		this.smppSess.timer.schedule(responseNotReceived, this.smppRA.getSmppResponseReceivedTimeout(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Cancel the timers
	 */
	protected void cancelResponseNotSentTimeout() {
		if (responseNotSent != null) {
			responseNotSent.cancel();
		}
	}

	protected void cancelResponseNotReceivedTimeout() {
		if (responseNotReceived != null) {
			responseNotReceived.cancel();
		}
	}

	protected void sendGenericNack() {
		GenericNackImpl genericNak = new GenericNackImpl(SmppTransaction.ESME_RUNKNOWNERR);
		genericNak.setSequenceNum(this.requestPDU.getSequenceNum());
		try {
			this.smppRA.sendResponse(genericNak);
		} catch (IOException e) {
			tracer.severe("IOException while sending GenericNack SMPP Response", e);
		}
	}

	private static class ResponseNotSent implements Runnable {

		private final SmppTransactionImpl tx;

		private boolean cancelled = false;

		public ResponseNotSent(SmppTransactionImpl tx) {
			this.tx = tx;
		}
		
		public void run() {
			if(cancelled) {
				return;
			}
			tx.responseNotSent();
		}

		public void cancel() {
			cancelled = true;
		}
	}

	private void responseNotSent() {
		// Send GENERIC_NACK to back SMSC
		sendGenericNack();
		// Fire SMPP_TIMEOUT_RESPONSE_SENT back to application
		SmppErrorImpl error = new SmppErrorImpl(SmppError.SMPP_TIMEOUT_RESPONSE_SENT, this.requestPDU);
		this.smppRA.fireEvent(Utils.SMPP_TIMEOUT_RESPONSE_SENT, this, error);
		this.smppRA.endActivity(this);
	}
	
	private static class ResponseNotReceived implements Runnable {

		private final SmppTransactionImpl tx;
		private boolean cancelled = false;
		
		public ResponseNotReceived(SmppTransactionImpl tx) {
			this.tx = tx;
		}
		
		public void run() {
			if(cancelled) {
				return;
			}
			tx.responseNotReceived();
		}

		public void cancel() {
			cancelled = true;			
		}
	}

	private void responseNotReceived() {
			// Fire SMPP_TIMEOUT_RESPONSE_RECEIVED back to application
		SmppErrorImpl error = new SmppErrorImpl(SmppError.SMPP_TIMEOUT_RESPONSE_RECEIVED, this.requestPDU);
		this.smppRA.fireEvent(Utils.SMPP_TIMEOUT_RESPONSE_RECEIVED, this, error);
		this.smppRA.endActivity(this);
	}
	
	@Override
	public int hashCode() {
		return handle.hashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SmppTransactionImpl other = (SmppTransactionImpl) obj;
		return handle.equals(other.handle);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TransactionImpl[SequenceNumber = ").append(sequenceNumber).append(", Hash = ").append(
				this.hashCode()).append("]");
		return sb.toString();
	}

}
