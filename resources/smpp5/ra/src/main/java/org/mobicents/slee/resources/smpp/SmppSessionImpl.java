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
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.slee.facilities.Tracer;

import net.java.slee.resources.smpp.SmppSession;
import net.java.slee.resources.smpp.SmppTransaction;
import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.PDU;
import net.java.slee.resources.smpp.pdu.SmppRequest;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.util.AbsoluteSMPPDate;
import net.java.slee.resources.smpp.util.RelativeSMPPDate;

import org.mobicents.slee.resources.smpp.pdu.AddressImpl;
import org.mobicents.slee.resources.smpp.pdu.AlertNotificationImpl;
import org.mobicents.slee.resources.smpp.pdu.CancelSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DataSMImpl;
import org.mobicents.slee.resources.smpp.pdu.DeliverSMImpl;
import org.mobicents.slee.resources.smpp.pdu.QuerySMImpl;
import org.mobicents.slee.resources.smpp.pdu.ReplaceSMImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitMultiImpl;
import org.mobicents.slee.resources.smpp.pdu.SubmitSMImpl;
import org.mobicents.slee.resources.smpp.util.AbsoluteSMPPDateImpl;
import org.mobicents.slee.resources.smpp.util.RelativeSMPPDateImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SmppSessionImpl implements SmppSession {

	private static Tracer tracer;

	private String sessionId;
	private SmppResourceAdaptor smppResourceAdaptor = null;
	private boolean isAlive = false;

	protected ScheduledExecutorService timer = Executors.newScheduledThreadPool(4);

	public SmppSessionImpl(SmppResourceAdaptor smppResourceAdaptor) {
		this.smppResourceAdaptor = smppResourceAdaptor;
		if (tracer == null) {
			tracer = this.smppResourceAdaptor.getRAContext().getTracer(
					SmppSessionImpl.class.getSimpleName());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SmppSession[SMSHost=")
				.append(this.smppResourceAdaptor.getHost())
				.append(", SMSCPort=")
				.append(this.smppResourceAdaptor.getPort())
				.append(", SystemId")
				.append(this.smppResourceAdaptor.getSystemId());

		this.sessionId = sb.toString();

	}

	public Address createAddress(int addTon, int addNpi, String address) {
		return new AddressImpl(addTon, addNpi, address);
	}

	public SmppRequest createSmppRequest(int commandId) {
		SmppRequest request = null;
		if (commandId == SmppRequest.ALERT_NOTIFICATION) {
			request = new AlertNotificationImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.CANCEL_SM) {
			request = new CancelSMImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.DATA_SM) {
			request = new DataSMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.DELIVER_SM) {
			request = new DeliverSMImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.QUERY_SM) {
			request = new QuerySMImpl(this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.REPLACE_SM) {
			request = new ReplaceSMImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.SUBMIT_MULTI) {
			request = new SubmitMultiImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		} else if (commandId == SmppRequest.SUBMIT_SM) {
			request = new SubmitSMImpl(
					this.smppResourceAdaptor.seq.nextNumber());
		}

		return request;
	}

	public AbsoluteSMPPDate createAbsoluteSMPPDate(Calendar calendar,
			boolean hasTz) {
		return new AbsoluteSMPPDateImpl(calendar, hasTz);
	}

	public RelativeSMPPDate createRelativeSMPPDate(int years, int months,
			int days, int hours, int minutes, int seconds) {
		return new RelativeSMPPDateImpl(years, months, days, hours, minutes,
				seconds);
	}

	public String getSMSCHost() {
		return this.smppResourceAdaptor.getHost();
	}

	public int getSMSPort() {
		return this.smppResourceAdaptor.getPort();
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public SmppTransaction createTransaction(SmppRequest request) {
		return getSmppTransactionImpl(request, true,
				SmppTransactionType.OUTGOING);
	}

	public void sendRequest(SmppRequest request) throws IllegalStateException,
			NullPointerException, IOException {

		if (!this.isAlive()) {
			throw new IllegalStateException("The ESME is not connected to SMSC");
		}

		if (request == null) {
			throw new NullPointerException("SMPP Request cannot be null");
		}

		SmppTransactionImpl smppTxImpl = this.getSmppTransactionImpl(request,
				false, SmppTransactionType.OUTGOING);
		if (smppTxImpl != null) {
			this.smppResourceAdaptor.sendRequest((ExtSmppRequest) request);
		}
	}

	public void sendResponse(SmppTransaction txn, SmppResponse response)
			throws IllegalStateException, NullPointerException, IOException {

		if (!this.isAlive()) {
			throw new IllegalStateException("The ESME is not connected to SMSC");
		}

		if (response == null) {
			throw new NullPointerException("SMPP Response cannot be null");
		}

		SmppTransactionImpl smppTxImpl = this.getSmppTransactionImpl(response, false, SmppTransactionType.INCOMING);
		try{
			smppTxImpl.cancelResponseNotSentTimeout();
	
			this.smppResourceAdaptor.sendResponse((ExtSmppResponse) response);
		} finally {
			//Kill Activity
			this.smppResourceAdaptor.endActivity(smppTxImpl);
		}

	}

	protected void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * This method will return the existing SmppTransaction (Activity) if
	 * already exist else create new if creayeActivity is true. If already exist
	 * we remove it from transactions Map as this Tx life is only till response
	 * is received back
	 * 
	 * @param pdu
	 * @param createActivity
	 * @param requestReceived
	 * @return
	 */
	protected SmppTransactionImpl getSmppTransactionImpl(PDU pdu,
			boolean createActivity, SmppTransactionType txType) {
		
		final SmppTransactionHandle handle = new SmppTransactionHandle(pdu.getSequenceNum(),txType);
		final ConcurrentHashMap<SmppTransactionHandle, SmppTransactionImpl> txMap = smppResourceAdaptor.getHandleVsActivityMap();
		
		SmppTransactionImpl txImpl = null;
		if (!createActivity) {
			txImpl = txMap.get(handle);
			if (txImpl != null) {
				if (tracer.isFineEnabled()) {
					tracer.fine("Got the SmppTransaction " + txImpl);
				}
				return txImpl;
			}
		}
		if (tracer.isFineEnabled()) {
			tracer
					.fine("Didnt get the SmppTransaction and createActivity = "
							+ createActivity + " and SmppTransactionType = "
							+ txType + " For PDU " + pdu + " Seq No = "
							+ pdu.getSequenceNum());
		}
		// New Activity only created when either new SMPP Request arrives or
		// Service sending new SMPP Request
		if (createActivity) {
			
			txImpl = new SmppTransactionImpl(handle,(SmppRequest) pdu,
					this.smppResourceAdaptor, this);
			boolean activityStarted = false;
			try {
				txMap.put(handle, txImpl);

				switch (txType) {
				case INCOMING:
					// Try to start the Activity
					this.smppResourceAdaptor
							.startNewSmppTransactionActivity(txImpl);
					activityStarted = true;
					txImpl.setResponseNotSentTimeout();
					break;
				case OUTGOING:
					// Try to start the Activity in Suspended Mode
					this.smppResourceAdaptor
							.startNewSmppTransactionSuspendedActivity(txImpl);
					activityStarted = true;
					txImpl.setResponseNotReceivedTimeout();
					break;
				}

				return txImpl;
			} catch (Exception e) {
				tracer.severe(
						"Failed to start the Activity. SmppTransaction "
								+ txImpl, e);

				try {
					if (!activityStarted) {
						txMap.remove(handle);
					}
					switch (txType) {
					case INCOMING:
						txImpl.cancelResponseNotSentTimeout();
						txImpl.sendGenericNack();
						return null;
					case OUTGOING:
						txImpl.cancelResponseNotReceivedTimeout();
						throw new IllegalStateException(
								"Error while trying to create Activity");
					}
				} finally {
					if (activityStarted) {
						this.smppResourceAdaptor.endActivity(txImpl);
					}					
				}
			}
		} // if (createActivity)

		if (tracer.isFineEnabled()) {
			tracer
					.fine("Now we will throw exception. But before that lets just iterate");
			for (SmppTransactionHandle e : txMap.keySet()) {
				tracer.fine("Seq = " + e);
			}
		}
		throw new IllegalStateException("No Activity found for PDU " + pdu);
	}

	@Override
	public int hashCode() {
		final int prime = 19;
		int result = 1;
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SmppSessionImpl other = (SmppSessionImpl) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

}
