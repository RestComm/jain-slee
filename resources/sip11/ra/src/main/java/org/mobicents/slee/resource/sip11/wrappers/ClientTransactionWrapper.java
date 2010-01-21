package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sip.ClientTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.TransactionActivityHandle;

/**
 * 
 * Wrapper for a {@link ClientTransaction}
 *
 */
public class ClientTransactionWrapper extends TransactionWrapper implements
		ClientTransaction {

	private static final long serialVersionUID = 1L;
	private static Tracer tracer;
	
	/**
	 * the server tx associated
	 */
	private ClientTransactionAssociation association;
	
	/**
	 * the slee address where events on this tx are fired
	 */
	private transient Address eventFiringAddress;

	/**
	 * the wrapped client tx
	 */
	private transient ClientTransaction wrappedTransaction;
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ClientTransactionWrapper(ClientTransaction wrappedTransaction, SipResourceAdaptor ra) {
		super(new TransactionActivityHandle(wrappedTransaction
				.getBranchId(),wrappedTransaction.getRequest().getMethod()));
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		setResourceAdaptor(ra);
	}

	protected ClientTransactionWrapper(TransactionActivityHandle handle, SipResourceAdaptor ra) {
		super(handle);
		setResourceAdaptor(ra);
	}
	
	@Override
	public void setResourceAdaptor(SipResourceAdaptor ra) {
		super.setResourceAdaptor(ra);
		if (tracer == null) {
			tracer = ra.getTracer(ClientTransactionWrapper.class.getSimpleName());
		}
	}
	
	@Override
	public Transaction getWrappedTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * Retrieves the wrapped transaction.
	 * @return
	 */
	public ClientTransaction getWrappedClientTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * For future use on sip transaction fail over.
	 * @param wrappedTransaction
	 */
	public void setWrappedClientTransaction(ClientTransaction wrappedTransaction) {
		this.wrappedTransaction = wrappedTransaction;
	}
	
	@Override
	public boolean isAckTransaction() {		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#isClientTransaction()
	 */
	@Override
	public boolean isClientTransaction() {
		return true;
	}
	
	@Override
	public Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			eventFiringAddress = ClientTransactionWrapper.getEventFiringAddress(((FromHeader) wrappedTransaction.getRequest().getHeader(FromHeader.NAME))
					.getAddress());
		}
		return eventFiringAddress;
	}
	
	/**
	 * 
	 * @param fromAddress
	 * @return
	 */
	public static javax.slee.Address getEventFiringAddress(javax.sip.address.Address fromAddress) {
		return new javax.slee.Address(AddressPlan.SIP,
				fromAddress.toString());
	}
	
	//  javax.sip.ClientTransaction interface
	
	/*
	 * Helper to validate the wrapped transaction, before interacting with it.
	 */
	private void validateWrappedTransaction() throws IllegalStateException {
		if (wrappedTransaction == null) {
			throw new IllegalStateException();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.ClientTransaction#createAck()
	 */
	@SuppressWarnings("deprecation")
	public Request createAck() throws SipException {
		validateWrappedTransaction();
		return wrappedTransaction.createAck();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ClientTransaction#createCancel()
	 */
	public Request createCancel() throws SipException {
		validateWrappedTransaction();
		return wrappedTransaction.createCancel();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ClientTransaction#sendRequest()
	 */
	public void sendRequest() throws SipException {
		//hack to add this tx as cancelable, in case someone use x send, instead of dialog.send(ctx);
		validateWrappedTransaction();
		final String method = this.wrappedTransaction.getRequest().getMethod();
		final DialogWrapper dw = getDialogWrapper();
		if((method.equals(Request.INVITE) || method.equals(Request.SUBSCRIBE)) && dw != null) {
			dw.lastCancelableTransactionId = this.activityHandle;
		}
		if (tracer.isInfoEnabled()) {
			tracer.info(toString()+" sending request:\n"+getRequest());
		}
		wrappedTransaction.sendRequest();
	}

	/**
	 * 
	 * @param serverTransaction
	 * @param dialogHandle
	 */
	public void associateServerTransaction(ServerTransactionWrapper serverTransaction,
			SipActivityHandle dialogHandle) {

		if (this.association != null) {
			throw new IllegalStateException(
					"Transaction already associated to ["
							+ this.association.getAssociatedServerTransaction() + "] ["
							+ this.association.getDialogActivityHandle() + "]");

		}
		this.association = new ClientTransactionAssociation(dialogHandle,serverTransaction.getActivityHandle());
	}

	/**
	 * 
	 * @return
	 */
	public ClientTransactionAssociation getClientTransactionAssociation() {
		return association;
	}
	
	@Override
	public String toString() {
		final String id = wrappedTransaction == null ? "null" : wrappedTransaction.getBranchId();
		return new StringBuilder("ClientTransaction[").append(id)
			.append(']').toString();
	}
	
	// SERIALIZATION
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		// write everything not static or transient
		stream.defaultWriteObject();
        //stream.writeUTF(wrappedTransaction.getBranchId());
	}
	
	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		//final String branchId = stream.readUTF();
		// TODO get tx from stack by branch id
		activityHandle.setActivity(this);
	}
	
	@Override
	public void terminated() {
		if (isActivity()) {
			final DialogWrapper dw = getDialogWrapper();
			if (dw != null) {
				dw.removeOngoingTransaction(this);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.Wrapper#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		if (wrappedTransaction != null) {
			wrappedTransaction.setApplicationData(null);
			wrappedTransaction = null;
		}		
		eventFiringAddress = null;
		association = null;
	}
}
