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

import gov.nist.javax.sip.stack.SIPClientTransaction;

import javax.sip.ClientTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.sip11.ClientTransactionActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * 
 * Wrapper for a {@link ClientTransaction}
 *
 */
public class ClientTransactionWrapper extends TransactionWrapper implements
		ClientTransaction {

	private static final long serialVersionUID = 1L;
	private static Tracer tracer;
	
	/**
	 * the wrapped client tx
	 */
	private ClientTransaction wrappedTransaction;
	
	/**
	 * the slee address where events on this tx are fired
	 */
	private transient Address eventFiringAddress;
	
	/**
	 * the associated stx
	 */
	private String associatedServerTransactionId;
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ClientTransactionWrapper(SIPClientTransaction wrappedTransaction, SipResourceAdaptor ra) {
		super(new ClientTransactionActivityHandle(wrappedTransaction.getTransactionId()),ra);
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(new ClientTransactionWrapperAppData(this));
		if (tracer == null) {
			tracer = ra.getTracer(ClientTransactionWrapper.class.getSimpleName());
		}
	}

	protected ClientTransactionWrapper(ClientTransactionActivityHandle handle, SipResourceAdaptor ra) {
		super(handle,ra);		
		if (tracer == null) {
			tracer = ra.getTracer(ClientTransactionWrapper.class.getSimpleName());
		}
	}
	
	@Override
	public Transaction getWrappedTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * Retrieves the wrapped transaction.
	 * @return
	 */
	public ClientTransaction getWrappedClientTransaction() {
		return wrappedTransaction;
	}
	
	@Override
	public boolean isAckTransaction() {		
		return false;
	}
	
	@Override
	public Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			eventFiringAddress = ClientTransactionWrapper.getEventFiringAddress(((FromHeader) wrappedTransaction.getRequest().getHeader(FromHeader.NAME))
					.getAddress());
		}
		return eventFiringAddress;
	}
	
	/**
	 * 
	 * @param fromAddress
	 * @return
	 */
	public static javax.slee.Address getEventFiringAddress(javax.sip.address.Address fromAddress) {
		return new javax.slee.Address(AddressPlan.SIP,
				fromAddress.toString());
	}
	
	//  javax.sip.ClientTransaction interface
	
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
	 * @see javax.sip.ClientTransaction#createAck()
	 */
	@SuppressWarnings("deprecation")
	public Request createAck() throws SipException {
		validateWrappedTransaction();
		return wrappedTransaction.createAck();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ClientTransaction#createCancel()
	 */
	public Request createCancel() throws SipException {
		validateWrappedTransaction();
		return wrappedTransaction.createCancel();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ClientTransaction#sendRequest()
	 */
	public void sendRequest() throws SipException {
		//hack to add this tx as cancelable, in case someone use x send, instead of dialog.send(ctx);
		validateWrappedTransaction();
		final String method = this.wrappedTransaction.getRequest().getMethod();
		final DialogWrapper dw = getDialogWrapper();
		if((method.equals(Request.INVITE) || method.equals(Request.SUBSCRIBE)) && dw != null) {
			dw.lastCancelableTransactionId = this.activityHandle;
		}
		if (tracer.isInfoEnabled()) {
			tracer.info(toString()+" sending request:\n"+getRequest());
		}
		wrappedTransaction.sendRequest();
	}

	/**
	 * 
	 * @return
	 */
	public String getAssociatedServerTransaction() {
		return associatedServerTransactionId;
	}
	
	/**
	 * 
	 * @param associatedServerTransactionId
	 */
	public void setAssociatedServerTransaction(
			String associatedServerTransactionId, boolean failIfAlreadyAssociated) {
		if (failIfAlreadyAssociated && this.associatedServerTransactionId != null) {
			throw new IllegalStateException(
					"Transaction already associated to ["
							+ associatedServerTransactionId + "]");
		}
		this.associatedServerTransactionId = associatedServerTransactionId;		
	}
	
	@Override
	public String toString() {
		final String id = wrappedTransaction == null ? "null" : wrappedTransaction.getBranchId();
		return new StringBuilder("ClientTransaction[").append(id)
			.append(']').toString();
	}
	
	@Override
	public void terminated() {
		final DialogWrapper dw = getDialogWrapper();
		if (dw != null) {
			dw.removeOngoingTransaction(this);			
		}
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

	@Override
	public boolean isClientTransaction() {
		return true;
	}
	
}
