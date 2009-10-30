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
	}

	public TransactionTerminatedEventWrapper(Object source, ServerTransaction tx) {
		super(source, tx);
	}
	
	public String toString() {
		
		Transaction tx=null;
		if(super.getClientTransaction()==null)
			tx=super.getServerTransaction();
		else
			tx=super.getClientTransaction();
		
		return new StringBuilder("[TransactionTerminatedEventWrapper ISSERVER[").append((super.getServerTransaction()!=null))
			.append("] BRANCHID[").append(tx.getBranchId())
			.append("] STATE[").append(tx.getState())
			.append("] METHOD[").append(tx.getRequest().getMethod())
			.append("] DIALOG{ ").append(tx.getDialog()).append(" } ]").toString();
	}
	
}
