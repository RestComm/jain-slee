package org.mobicents.slee.resource.sip11.wrappers;

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
		return dialog != null ? (DialogWrapper) dialog.getApplicationData() : null;
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
}
