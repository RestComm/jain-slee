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

import gov.nist.javax.sip.stack.SIPServerTransaction;

import java.text.ParseException;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.sip11.ServerTransactionActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * 
 * A wrapper for {@link ServerTransaction}
 *
 */
public class ServerTransactionWrapper extends TransactionWrapper implements ServerTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final transient boolean ackTransaction;
		
	private transient Address eventFiringAddress;
	
	private static Tracer tracer;
	
	private transient ServerTransaction wrappedTransaction;
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ServerTransactionWrapper(ACKDummyTransaction wrappedTransaction, SipResourceAdaptor ra) {
		this(wrappedTransaction.getTxId(),wrappedTransaction,ra,true);
	}
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ServerTransactionWrapper(SIPServerTransaction wrappedTransaction, SipResourceAdaptor ra) {
		this(wrappedTransaction.getTransactionId(),wrappedTransaction,ra,wrappedTransaction.getRequest().getMethod().equals(Request.ACK));
	}

	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 * @param ackTransaction
	 */
	private ServerTransactionWrapper(String txId, ServerTransaction wrappedTransaction, SipResourceAdaptor ra, boolean ackTransaction) {
		super(new ServerTransactionActivityHandle(txId),ra);
		this.ackTransaction = ackTransaction;
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(new ServerTransactionWrapperAppData(this));		
		if (tracer == null) {
			tracer = ra.getTracer(ServerTransactionWrapper.class.getSimpleName());
		}
	}
	
	@Override
	public Transaction getWrappedTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * 
	 * @return
	 */
	public ServerTransaction getWrappedServerTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * For future use on sip transaction fail over.
	 * @param wrappedTransaction
	 */
	public void setWrappedServerTransaction(ServerTransaction wrappedTransaction) {
		this.wrappedTransaction = wrappedTransaction;
	}
	
	@Override
	public boolean isAckTransaction() {
		return ackTransaction;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#isClientTransaction()
	 */
	@Override
	public boolean isClientTransaction() {
		return false;
	}
	
	/*
	 * Helper to validate the wrapped transaction, before interacting with it.
	 */
	private void validateWrappedTransaction() throws IllegalStateException {
		if (wrappedTransaction == null) {
			throw new IllegalStateException();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface#getEventFiringAddress()
	 */
	public Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			eventFiringAddress = new Address(AddressPlan.SIP,
					((ToHeader) wrappedTransaction.getRequest().getHeader(ToHeader.NAME))
					.getAddress().toString());
		}
		return eventFiringAddress;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.ServerTransaction#enableRetransmissionAlerts()
	 */
	public void enableRetransmissionAlerts() throws SipException {
		validateWrappedTransaction();
		wrappedTransaction.enableRetransmissionAlerts();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ServerTransaction#sendResponse(javax.sip.message.Response)
	 */
	public void sendResponse(Response arg0) throws SipException, InvalidArgumentException {
		
		validateWrappedTransaction();
		
		final Dialog d = wrappedTransaction.getDialog();
		if (d != null) {
			final DialogWrapper dw = ra.getDialogWrapper(d);
			if (dw != null) {
				final int statusCode = arg0.getStatusCode();
				if (this.getRequest().getMethod().equals(Request.CANCEL) && (statusCode < 300 && statusCode > 199) && dw.getState() == null ) {
					dw.delete();
				}
				else if (d.getLocalTag() == null) {
					// a new dialog which local tag is not yet known by jain sip stack, add the tag to the response 
					try {
						((ToHeader)arg0.getHeader(ToHeader.NAME)).setTag(dw.getLocalTag());
					} catch (ParseException e) {
						throw new SipException(e.getMessage(),e);
					}
				}
			}
		}
		
		if (tracer.isInfoEnabled()) {
			tracer.info(toString()+" sending response:\n"+arg0);
		}
		wrappedTransaction.sendResponse(arg0);			
	}

	@Override
	public String toString() {
		final String id = wrappedTransaction == null ? String.valueOf(null) : wrappedTransaction.getBranchId(); 
		return new StringBuilder("ServerTransaction[").append(id).append(']').toString();		
	}

	
	@Override
	public void terminated() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.Wrapper#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		if (wrappedTransaction != null) {
			wrappedTransaction.setApplicationData(null);
		}
		wrappedTransaction = null;
	}
	
}
