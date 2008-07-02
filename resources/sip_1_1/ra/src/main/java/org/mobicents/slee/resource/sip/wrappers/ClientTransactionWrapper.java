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
	// protected Object applicationData = null;
	// protected Logger logger = Logger.getLogger(ClientTransactionWrapper.class
	// .getCanonicalName());
	// protected SipActivityHandle sipActivityHandle = null;

	protected ServerTransactionWrapper associatedServerTransaction = null;

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

	public void clearAssociations() {

		if (this.associatedServerTransaction != null)
			this.associatedServerTransaction.removeAssociatedTransaction(this);
		if (dialogWrapper != null)
			super.dialogWrapper.removeOngoingTransaction(this);

	}

	void removeAssociatedServerTransaction() {

		this.associatedServerTransaction = null;

	}

	public void setAssociatedServerTransaction(ServerTransactionWrapper stw) {
		if (this.associatedServerTransaction != null && stw != null) {
			throw new IllegalStateException(
					"Cant associate with another server transaction");
		}

		if (stw.getState() == TransactionState.TERMINATED) {
			throw new IllegalStateException(
					"Cant associate with another server transaction, state["
							+ stw.getState() + "]");
		}

		try {
			if (this.associatedServerTransaction != null)// just a precaution
				this.associatedServerTransaction
						.removeAssociatedTransaction(this);

			this.associatedServerTransaction = stw;
			if(associatedServerTransaction!=null)
				this.associatedServerTransaction.addAssociatedTransaction(this);

		} catch (Exception e) {
			this.associatedServerTransaction = null;
			throw new RuntimeException(
					"Failed to associate with server transaction due:", e);
		}

	}

	public ServerTransactionWrapper getAssociatedServerTransaction() {
		return this.associatedServerTransaction;
	}

	public String toString() {

		return "ClientTransaction BR[" + this.getBranchId() + "] METHOD["
				+ this.getRequest().getMethod() + "] STATE["
				+ this.wrappedTransaction.getState() + "]";

	}

}
