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

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.sip.Dialog;
import javax.sip.ObjectInUseException;
import javax.sip.Transaction;
import javax.sip.TransactionState;
import javax.sip.message.Request;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.TransactionActivityHandle;

/**
 * The base class for client and server transaction wrappers.
 * @author martins
 *
 */
public abstract class TransactionWrapper extends Wrapper implements Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private transient boolean activity = false;
	
	/**
	 * 
	 * @param activityHandle
	 */
	public TransactionWrapper(TransactionActivityHandle activityHandle, SipResourceAdaptor ra) {
		super(activityHandle,ra);
	}

	@Override
	public boolean isDialog() {
		return false;
	}
	
	/**
	 * Retrieves the wrapped sip transaction.
	 * @return
	 */
	public abstract Transaction getWrappedTransaction();

	/**
	 * Indicates if the wrapper is an activity. 
	 * @return
	 */
	public boolean isActivity() {
		return activity;
	}
	
	/**
	 * 
	 * @param activity
	 */
	public void setActivity(boolean activity) {
		this.activity = activity;
	}
	
	/**
	 * 
	 * @return
	 */
	public DialogWrapper getDialogWrapper() {
		final Dialog dialog = getAndValidateWrappedTransaction().getDialog();
		return dialog != null ? ra.getDialogWrapper(dialog) : null;
	}
	
	/**
	 * callback invoked when the underlying tx was terminated
	 */
	public abstract void terminated();
	
	/**
	 * Indicates if it is a client transaction, avoiding the check of class types 
	 * @return
	 */
	public abstract boolean isClientTransaction();
	
	// javax.sip.Transaction interface
	
	/*
	 * Helper to validate the wrapped transaction, before interacting with it.
	 */
	private Transaction getAndValidateWrappedTransaction() throws IllegalStateException {
		final Transaction wrappedTransaction = getWrappedTransaction();
		if (wrappedTransaction == null) {
			throw new IllegalStateException();
		}
		else { 
			return wrappedTransaction;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#getBranchId()
	 */
	public String getBranchId() {
		return getAndValidateWrappedTransaction().getBranchId();
	}
		
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#getDialog()
	 */
	public Dialog getDialog() {
		return getDialogWrapper();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#getRequest()
	 */
	public Request getRequest() {
		return getAndValidateWrappedTransaction().getRequest();
	}
		
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#getRetransmitTimer()
	 */
	public int getRetransmitTimer() throws UnsupportedOperationException {
		return getAndValidateWrappedTransaction().getRetransmitTimer();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#getState()
	 */
	public TransactionState getState() {
		return getAndValidateWrappedTransaction().getState();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#setRetransmitTimer(int)
	 */
	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		getAndValidateWrappedTransaction().setRetransmitTimer(arg0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Transaction#terminate()
	 */
	public void terminate() throws ObjectInUseException {
		getAndValidateWrappedTransaction().terminate();
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		throw new IOException("serialization forbidden");
	}
	
}
