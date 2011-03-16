package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.stack.SIPClientTransaction;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.sip.Transaction;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public class ClientTransactionWrapperAppData implements TransactionWrapperAppData, Externalizable {

	private ClientTransactionWrapper transactionWrapper;
	private String associatedServerTransactionId;
	
	public ClientTransactionWrapperAppData(ClientTransactionWrapper transactionWrapper) {
		this.transactionWrapper = transactionWrapper;
	}
	
	public ClientTransactionWrapperAppData() {
		
	}
	
	
	@Override
	public TransactionWrapper getTransactionWrapper(Transaction t,
			SipResourceAdaptor ra) {
		if (transactionWrapper == null) {
			transactionWrapper = new ClientTransactionWrapper((SIPClientTransaction) t, ra);
			transactionWrapper.setAssociatedServerTransaction(associatedServerTransactionId,false);
		}
		return transactionWrapper;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		if (transactionWrapper != null) {
			associatedServerTransactionId = transactionWrapper.getAssociatedServerTransaction();
		}
		if (associatedServerTransactionId != null) {
			out.writeBoolean(true);
			out.writeUTF(associatedServerTransactionId);
		}
		else {
			out.writeBoolean(false);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		if (in.readBoolean()) {
			associatedServerTransactionId = in.readUTF();
		}
	}

}
