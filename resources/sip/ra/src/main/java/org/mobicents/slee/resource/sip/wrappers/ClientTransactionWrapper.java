package org.mobicents.slee.resource.sip.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ObjectInUseException;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionState;
import javax.sip.message.Request;

import org.mobicents.slee.resource.sip.SipActivityHandle;

public class ClientTransactionWrapper implements javax.sip.ClientTransaction,
		SecretWrapperInterface {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientTransaction realTransaction = null;
	private Object applicationData = null;
	private DialogWrapper dialogWrapper;
	private SipActivityHandle activityHandle=null;
	public ClientTransactionWrapper(ClientTransaction CT,
			DialogWrapper dialogWrapper) {
		this.realTransaction = CT;
		CT.setApplicationData(this);
		this.dialogWrapper = dialogWrapper;
		
		this.activityHandle=new SipActivityHandle(CT.getBranchId()+"_"+CT.getRequest().getMethod());
	}

	public Transaction getRealTransaction() {
		return realTransaction;
	}
	
	public void setDialogWrapper(DialogWrapper dialogWrapper) {
		this.dialogWrapper = dialogWrapper;
	}
	
	public void sendRequest() throws SipException {
		realTransaction.sendRequest();
		if (dialogWrapper != null) {
			dialogWrapper.renew();
		}
	}

	public Request createCancel() throws SipException {

		return realTransaction.createCancel();
	}

	public Request createAck() throws SipException {

		return realTransaction.createAck();
	}

	public Dialog getDialog() {
		// TODO Auto-generated method stub
		return dialogWrapper;
	}

	public TransactionState getState() {
		// TODO Auto-generated method stub
		return realTransaction.getState();
	}

	public int getRetransmitTimer() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return realTransaction.getRetransmitTimer();
	}

	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		realTransaction.setRetransmitTimer(arg0);

	}

	public String getBranchId() {
		return realTransaction.getBranchId();	
	}

	public Request getRequest() {
		// TODO Auto-generated method stub
		return realTransaction.getRequest();
	}

	public void setApplicationData(Object appData) {
		this.applicationData = appData;

	}

	public Object getApplicationData() {

		return applicationData;
	}

	public void terminate() throws ObjectInUseException {
		realTransaction.terminate();

	}

	public SipActivityHandle getActivityHandle() {

		return this.activityHandle;
	}

	public String toString() {
		return "[TransactionW[" + super.toString() + "] WRAPPED["
				+ realTransaction + "] BRANCHID["
				+ realTransaction.getBranchId() + "] STATE["
				+ realTransaction.getState() + "] DIALOG{ " + dialogWrapper
				+ " } ]";
	}
}
