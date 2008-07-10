package org.mobicents.slee.resource.sip.wrappers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ObjectInUseException;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;

import org.mobicents.slee.resource.sip.SipActivityHandle;

public class ClientTransactionWrapper extends SuperTransactionWrapper implements
		ClientTransaction, WrapperSuperInterface {

	protected ClientTransaction wrappedTransaction = null;


	// keeps handle for dialog from which we associated stx - branch above
	protected SipActivityHandle associationHandle = null;
	// keeps branch id of associated stx
	protected String associatedTransactionBranchId = null;

	public ClientTransactionWrapper(ClientTransaction wrappedTransaction) {
		if (wrappedTransaction.getApplicationData() != null) {
			if (wrappedTransaction.getApplicationData() instanceof ClientTransactionWrapper) {
				throw new IllegalArgumentException(
						"ClientTransaction to wrap has alredy a wrapper!!!");
			} else {
				if (logger.isLoggable(Level.FINER)) {
					logger.finer("Overwriting application data present - "
							+ wrappedTransaction.getApplicationData());
				}
			}
		}
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		this.sipActivityHandle = new SipActivityHandle(wrappedTransaction
				.getBranchId()
				+ "_" + wrappedTransaction.getRequest().getMethod());

	}

	public Request createAck() throws SipException {
		return wrappedTransaction.createAck();
	}

	public Request createCancel() throws SipException {
		return wrappedTransaction.createCancel();
	}

	public String getBranchId() {
		return wrappedTransaction.getBranchId();
	}

	public Request getRequest() {
		return wrappedTransaction.getRequest();
	}

	public int getRetransmitTimer() throws UnsupportedOperationException {
		return wrappedTransaction.getRetransmitTimer();
	}

	public TransactionState getState() {
		return wrappedTransaction.getState();
	}

	public void sendRequest() throws SipException {
		wrappedTransaction.sendRequest();
	}

	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		wrappedTransaction.setRetransmitTimer(arg0);
	}

	public void terminate() throws ObjectInUseException {
		wrappedTransaction.terminate();
	}

	public Object getWrappedObject() {
		return this.wrappedTransaction;
	}

	public String toString() {

		return "ClientTransaction BId[" + this.getBranchId() + "] METHOD["
				+ this.getRequest().getMethod() + "] STATE["
				+ this.wrappedTransaction.getState() + "]";

	}

	public void clearAssociations() {

		this.associatedTransactionBranchId = null;
		this.associationHandle = null;

	}

	public synchronized void associateServerTransaction(String branch,
			SipActivityHandle dialogHandle) {

		if (this.associatedTransactionBranchId != null && branch!=null) {
			throw new IllegalStateException(
					"Transaction already associated to ["
							+ this.associatedTransactionBranchId + "] ["
							+ this.associationHandle + "]");

		}
		this.associatedTransactionBranchId = branch;
		this.associationHandle = dialogHandle;
		
	}

	public SipActivityHandle getAssociationHandle() {
		return this.associationHandle;
	}

	public String getAssociatedTransactionBranchId() {
		return this.associatedTransactionBranchId;
	}

}
