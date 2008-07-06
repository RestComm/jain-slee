package org.mobicents.slee.resource.sip.wrappers;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.mobicents.slee.resource.sip.SipActivityHandle;

public class ServerTransactionWrapper extends SuperTransactionWrapper implements
		ServerTransaction, WrapperSuperInterface {

	protected ServerTransaction wrappedTransaction = null;


	// SLEE 1.1. specs stuff
	protected Set<ClientTransactionWrapper> associatedClientTransactions = new HashSet<ClientTransactionWrapper>();

	//protected static final Logger logger = Logger
	//		.getLogger(ServerTransactionWrapper.class.getCanonicalName());

	public ServerTransactionWrapper(ServerTransaction wrappedTransaction) {
		if (wrappedTransaction.getApplicationData() != null) {
			if (wrappedTransaction.getApplicationData() instanceof ServerTransactionWrapper) {
				throw new IllegalArgumentException(
						"ServerTransaction to wrap has alredy a wrapper!!!");
			} else {
				if (logger.isLoggable(Level.FINER)) {
					logger.finer("Overwriting application data present - "
							+ wrappedTransaction.getApplicationData());
				}
			}
		}
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		super.sipActivityHandle = new SipActivityHandle(wrappedTransaction
				.getBranchId()
				+ "_" + wrappedTransaction.getRequest().getMethod());

	}

	public void enableRetransmissionAlerts() throws SipException {
		wrappedTransaction.enableRetransmissionAlerts();
	}

	public void sendResponse(Response arg0) throws SipException,
			InvalidArgumentException {
		wrappedTransaction.sendResponse(arg0);
		String method=this.getRequest().getMethod();
		int statusCode=arg0.getStatusCode();
		if(method.equals(Request.CANCEL) && (statusCode<300 && statusCode>199))
		{
			if(super.dialogWrapper!=null && super.dialogWrapper.getState()==null)
				super.dialogWrapper.delete();
		}
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

	public void addAssociatedTransaction(ClientTransactionWrapper ctw) {
		// FIXME: WOuldnt it be better to check also for state?
		if(this.dialogWrapper.getState()==DialogState.TERMINATED || ctw.getDialog().getState()==DialogState.TERMINATED)
		{
			throw new IllegalStateException("!!");
		}
		
		if (!((DialogWrapper) ctw.getDialog()).hasOngoingClientTransaction(ctw
				.getBranchId())) {
			throw new IllegalArgumentException(
					"transaction that is going to be associated is not running in any thread");
		}

		if (!this.dialogWrapper.hasOngoingServerTransaction(this.getBranchId())) {
			throw new IllegalStateException(
					"Cant associate current transaction, its not running in dialog!!!");
		}
		synchronized (this.associatedClientTransactions) {
			this.associatedClientTransactions.add(ctw);
		}
	}

	public void removeAssociatedTransaction(ClientTransactionWrapper ctw) {
		synchronized (this.associatedClientTransactions) {
			this.associatedClientTransactions.remove(ctw);
		}
	}

	public void clearAssociations() {

		synchronized (this.associatedClientTransactions) {
			for (ClientTransactionWrapper ctw : this.associatedClientTransactions) {
				ctw.removeAssociatedServerTransaction();
			}

			this.associatedClientTransactions.clear();
		}
		if(dialogWrapper!=null)
			super.dialogWrapper.removeOngoingTransaction(this);
	}

	public String toString()
	{
		
		return "ServerTransaction BR["+this.getBranchId()+"] METHOD["+this.getRequest().getMethod()+"] STATE["+this.wrappedTransaction.getState()+"]";
		
	}
	
	/**
	 * Returns SipActviitytHandle for invite request. Should be called only by cancel
	 * @return
	 */
	public SipActivityHandle getInviteHandle()
	{
		String id=this.sipActivityHandle.getID();
		id=id.replace("_"+this.wrappedTransaction.getRequest().getMethod(), "_"+Request.INVITE).intern();
		return new SipActivityHandle(id);
	}
	
}
