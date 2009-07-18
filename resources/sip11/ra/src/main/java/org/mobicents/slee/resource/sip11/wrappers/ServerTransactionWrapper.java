package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ServerTransactionWrapper extends SuperTransactionWrapper implements ServerTransaction,
		WrapperSuperInterface {

	public ServerTransactionWrapper(ServerTransaction wrappedTransaction) {
		if (wrappedTransaction.getApplicationData() != null) {
			if (wrappedTransaction.getApplicationData() instanceof ServerTransactionWrapper) {
				throw new IllegalArgumentException("ServerTransaction to wrap has alredy a wrapper!!!");
			}
		}
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		super.sipActivityHandle = new SipActivityHandle(wrappedTransaction.getBranchId() + "_"
				+ wrappedTransaction.getRequest().getMethod());

	}

	public ServerTransactionWrapper() {
	}

	public void enableRetransmissionAlerts() throws SipException {
		((ServerTransaction) wrappedTransaction).enableRetransmissionAlerts();
	}

	public void sendResponse(Response arg0) throws SipException, InvalidArgumentException {
		((ServerTransaction) wrappedTransaction).sendResponse(arg0);
		String method = this.getRequest().getMethod();
		int statusCode = arg0.getStatusCode();
		if (method.equals(Request.CANCEL) && (statusCode < 300 && statusCode > 199)) {
			Dialog dialog = getDialog();
			if (dialog!=null && dialog.getState() == null)
				dialog.delete();
		}
	}

	public Dialog getDialog() {
		if (this.wrappedTransaction.getDialog() != null
				&& this.wrappedTransaction.getDialog().getApplicationData() != null) {
			return (DialogWrapper) this.wrappedTransaction.getDialog().getApplicationData();
		}
		return null;
	}

	public String getBranchId() {
		if(wrappedTransaction==null)
			return null;
		return wrappedTransaction.getBranchId();
	}

	public Request getRequest() {
		if(wrappedTransaction==null)
			return null;
		return wrappedTransaction.getRequest();
	}

	public int getRetransmitTimer() throws UnsupportedOperationException {
		if(wrappedTransaction==null)
			return -1;
		return wrappedTransaction.getRetransmitTimer();
	}

	public TransactionState getState() {
		if(wrappedTransaction==null)
			return null;
		return wrappedTransaction.getState();
	}

	public void setRetransmitTimer(int arg0) throws UnsupportedOperationException {
		if(wrappedTransaction==null)
			return;
		wrappedTransaction.setRetransmitTimer(arg0);
	}

	public void terminate() throws ObjectInUseException {
		if(wrappedTransaction==null)
			return ;
		wrappedTransaction.terminate();
	}

	public String toString() {

		
		String ret = "";
		ret+="ServerTransaction BR[" + this.getBranchId() + "] ";
		//Causes NPE, not even sure why. Values are not null.
		//ret+="METHOD[" + this.getRequest()==null?null:this.getRequest().getMethod() + "] ";
		//ret+="STATE["+ this.wrappedTransaction==null?null: this.wrappedTransaction.getState()+ "]";
		return ret;

	}

	/**
	 * Returns SipActviitytHandle for invite request. Should be called only by
	 * cancel
	 * 
	 * @return
	 */
	public SipActivityHandle getInviteHandle() {
		String id = this.sipActivityHandle.getID();
		id = id.replace("_" + this.wrappedTransaction.getRequest().getMethod(), "_" + Request.INVITE).intern();
		return new SipActivityHandle(id);
	}

	public void cleanup() {
		if(this.wrappedTransaction!=null)
		{
			this.wrappedTransaction.setApplicationData(null);
			this.wrappedTransaction = null;
		}
	}

}
