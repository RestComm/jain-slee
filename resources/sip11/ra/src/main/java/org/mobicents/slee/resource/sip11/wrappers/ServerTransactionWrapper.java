package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.TransactionActivityHandle;

/**
 * 
 * A wrapper for {@link ServerTransaction}
 *
 */
public class ServerTransactionWrapper extends TransactionWrapper implements ServerTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final boolean ackTransaction;
		
	private transient Address eventFiringAddress;
	
	private static Tracer tracer;
	
	private transient ServerTransaction wrappedTransaction;
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ServerTransactionWrapper(ACKDummyTransaction wrappedTransaction, SipResourceAdaptor ra) {
		this(wrappedTransaction,ra,true);
	}
	
	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 */
	public ServerTransactionWrapper(ServerTransaction wrappedTransaction, SipResourceAdaptor ra) {
		this(wrappedTransaction,ra,wrappedTransaction.getRequest().getMethod().equals(Request.ACK));
	}

	/**
	 * 
	 * @param wrappedTransaction
	 * @param ra
	 * @param ackTransaction
	 */
	private ServerTransactionWrapper(ServerTransaction wrappedTransaction, SipResourceAdaptor ra, boolean ackTransaction) {
		super(new TransactionActivityHandle(wrappedTransaction.getBranchId(),wrappedTransaction.getRequest().getMethod()));
		this.ackTransaction = ackTransaction;
		this.wrappedTransaction = wrappedTransaction;
		this.wrappedTransaction.setApplicationData(this);
		if (tracer == null) {
			tracer = ra.getTracer(ServerTransactionWrapper.class.getSimpleName());
		}
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
	 * 
	 * @return
	 */
	public ServerTransaction getWrappedServerTransaction() {
		return wrappedTransaction;
	}
	
	/**
	 * For future use on sip transaction fail over.
	 * @param wrappedTransaction
	 */
	public void setWrappedServerTransaction(ServerTransaction wrappedTransaction) {
		this.wrappedTransaction = wrappedTransaction;
	}
	
	@Override
	public boolean isAckTransaction() {
		return ackTransaction;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#isClientTransaction()
	 */
	@Override
	public boolean isClientTransaction() {
		return false;
	}
	
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
	 * @see org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface#getEventFiringAddress()
	 */
	public Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			eventFiringAddress = new Address(AddressPlan.SIP,
					((ToHeader) wrappedTransaction.getRequest().getHeader(ToHeader.NAME))
					.getAddress().toString());
		}
		return eventFiringAddress;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.ServerTransaction#enableRetransmissionAlerts()
	 */
	public void enableRetransmissionAlerts() throws SipException {
		validateWrappedTransaction();
		wrappedTransaction.enableRetransmissionAlerts();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.ServerTransaction#sendResponse(javax.sip.message.Response)
	 */
	public void sendResponse(Response arg0) throws SipException, InvalidArgumentException {
		
		validateWrappedTransaction();
		
		final Dialog d = wrappedTransaction.getDialog();
		if (d != null) {
			final DialogWrapper dw = (DialogWrapper) d.getApplicationData();
			if (dw != null) {
				final int statusCode = arg0.getStatusCode();
				if (this.getRequest().getMethod().equals(Request.CANCEL) && (statusCode < 300 && statusCode > 199) && dw.getState() == null ) {
					dw.delete();
				}
				else if (d.getLocalTag() == null) {
					// a new dialog which local tag is not yet known by jain sip stack, add the tag to the response 
					try {
						((ToHeader)arg0.getHeader(ToHeader.NAME)).setTag(dw.getLocalTag());
					} catch (ParseException e) {
						throw new SipException(e.getMessage(),e);
					}
				}
			}
		}
		
		if (tracer.isInfoEnabled()) {
			tracer.info(toString()+" sending response:\n"+arg0);
		}
		wrappedTransaction.sendResponse(arg0);			
	}

	@Override
	public String toString() {
		final String id = wrappedTransaction == null ? String.valueOf(null) : wrappedTransaction.getBranchId(); 
		return new StringBuilder("ServerTransaction[").append(id).append(']').toString();		
	}

	// SERIALIZATION

	private void writeObject(ObjectOutputStream stream) throws IOException {
		// write everything not static or transient
		stream.defaultWriteObject();
        /*if (ackTransaction) {
        	stream.writeObject(wrappedTransaction.getRequest());
        }
        else {
        	stream.writeUTF(wrappedTransaction.getBranchId());
        }*/
	}
	
	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		/*if (ackTransaction) {
			wrappedTransaction = new ACKDummyTransaction((Request) stream.readObject());
		}
		else {
			final String branchId = stream.readUTF();
			// TODO get tx from stack by branch id
		}*/
		activityHandle.setActivity(this);	
	}
	
	@Override
	public void terminated() {
		final DialogWrapper dw = getDialogWrapper();
		if (dw != null) {
			dw.removeOngoingTransaction(this);
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
	}
}
