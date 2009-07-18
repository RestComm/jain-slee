package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ObjectInUseException;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ClientTransactionWrapper extends SuperTransactionWrapper implements
		ClientTransaction, WrapperSuperInterface {

	// associated stx
	protected ClientTransactionAssociation association = null;
	
	public ClientTransactionWrapper(ClientTransaction wrappedTransaction) {
		if (wrappedTransaction.getApplicationData() != null) {
			if (wrappedTransaction.getApplicationData() instanceof ClientTransactionWrapper) {
				throw new IllegalArgumentException(
						"ClientTransaction to wrap has alredy a wrapper!!!");
			}
		}
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		this.sipActivityHandle = new SipActivityHandle(wrappedTransaction
				.getBranchId()
				+ "_" + wrappedTransaction.getRequest().getMethod());

	}

	public Dialog getDialog() {
		if (this.wrappedTransaction.getDialog() != null && this.wrappedTransaction.getDialog().getApplicationData() != null) {
			return (DialogWrapper) this.wrappedTransaction.getDialog().getApplicationData();
		}
		return null;
	}
	
	public Request createAck() throws SipException {
		return ((ClientTransaction) wrappedTransaction).createAck();
	}

	public Request createCancel() throws SipException {
		return ((ClientTransaction) wrappedTransaction).createCancel();
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
		//hack to add this tx as cancelable, in case someone use x send, instead of dialog.send(ctx);
		if(this.wrappedTransaction!=null)
		{
			String method =this.wrappedTransaction.getRequest().getMethod();
			if((method.equals(Request.INVITE) || method.equals(Request.SUBSCRIBE)) && getDialog()!=null)
			{
				DialogWrapper dw = (DialogWrapper) getDialog();
				dw.lastCancelableTransactionId = this.getBranchId();
			}
		}
		((ClientTransaction) wrappedTransaction).sendRequest();
	}

	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		wrappedTransaction.setRetransmitTimer(arg0);
	}

	public void terminate() throws ObjectInUseException {
		wrappedTransaction.terminate();
	}

	public String toString() {

		String returnValue = "ClientTransaction Id[" + (this.wrappedTransaction == null ? null : this.getBranchId()) + "] METHOD["
				+ (this.wrappedTransaction == null ? null : this.getRequest().getMethod()) + "] STATE["
				+ (this.wrappedTransaction == null ? null : this.wrappedTransaction.getState()) + "] handle[" + sipActivityHandle + "]";
	

		return returnValue;

	}

	public void associateServerTransaction(String branch,
			SipActivityHandle dialogHandle) {

		if (this.association != null) {
			throw new IllegalStateException(
					"Transaction already associated to ["
							+ this.association.getAssociatedTransactionBranchId() + "] ["
							+ this.association.getAssociationHandle() + "]");

		}
		this.association = new ClientTransactionAssociation(dialogHandle,branch);
	}

	public SipActivityHandle getAssociationHandle() {
		return this.association.getAssociationHandle();
	}

	public String getAssociatedTransactionBranchId() {
		if(this.association==null)
		{
			return null;
		}
		return this.association.getAssociatedTransactionBranchId();
	}

	public void cleanup() {
		this.wrappedTransaction.setApplicationData(null);
		this.association = null;
		this.wrappedTransaction = null;
	}
}
