package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.stack.SIPServerTransaction;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.sip.Transaction;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public class ServerTransactionWrapperAppData implements TransactionWrapperAppData, Externalizable {

	private ServerTransactionWrapper transactionWrapper;
	
	public ServerTransactionWrapperAppData(ServerTransactionWrapper transactionWrapper) {
		this.transactionWrapper = transactionWrapper;
	}
	
	public ServerTransactionWrapperAppData() {
		
	}
	
	
	@Override
	public TransactionWrapper getTransactionWrapper(Transaction t,
			SipResourceAdaptor ra) {
		if (transactionWrapper == null) {
			transactionWrapper = new ServerTransactionWrapper((SIPServerTransaction) t, ra);
		}
		return transactionWrapper;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// nothing
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// nothing
	}

}
