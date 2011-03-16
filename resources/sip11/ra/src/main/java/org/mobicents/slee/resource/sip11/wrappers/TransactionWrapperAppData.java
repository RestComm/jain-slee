package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Transaction;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public interface TransactionWrapperAppData {

	public TransactionWrapper getTransactionWrapper(Transaction t, SipResourceAdaptor ra);
	
}
