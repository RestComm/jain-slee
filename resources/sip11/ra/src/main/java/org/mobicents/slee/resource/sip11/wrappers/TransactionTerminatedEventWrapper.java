package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;

public class TransactionTerminatedEventWrapper extends
		TransactionTerminatedEvent {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7363453023732951366L;

	public TransactionTerminatedEventWrapper(Object source, ClientTransaction tx) {
		super(source, tx);
		// TODO Auto-generated constructor stub
	}

	public TransactionTerminatedEventWrapper(Object source, ServerTransaction tx) {
		super(source, tx);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		
		Transaction tx=null;
		if(super.getClientTransaction()==null)
			tx=super.getServerTransaction();
		else
			tx=super.getClientTransaction();
		
		return "[TransactionTerminatedEventWrapper ISSERVER[" + (super.getServerTransaction()!=null) + "] BRANCHID[" +tx.getBranchId()
				 + "] STATE["
				+ tx.getState() + "] METHOD["+tx.getRequest().getMethod()+"] DIALOG{ " + tx.getDialog()
				+ " } ]";
	}
	
}
