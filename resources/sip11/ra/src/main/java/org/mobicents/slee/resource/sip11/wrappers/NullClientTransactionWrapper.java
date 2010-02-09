/**
 * 
 */
package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Transaction;
import javax.slee.Address;

import org.mobicents.slee.resource.sip11.ClientTransactionActivityHandle;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * A dummy client tx wrapper for a non existent client transaction,
 * used when firing out of dialog late responses.
 * 
 * @author martins
 *
 */
public class NullClientTransactionWrapper extends ClientTransactionWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param activityHandle
	 */
	public NullClientTransactionWrapper(SipActivityHandle activityHandle, SipResourceAdaptor ra) {
		super((ClientTransactionActivityHandle) activityHandle,ra);
		setActivity(true);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#getWrappedTransaction()
	 */
	@Override
	public Transaction getWrappedTransaction() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.TransactionWrapper#terminated()
	 */
	@Override
	public void terminated() {}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.Wrapper#getEventFiringAddress()
	 */
	@Override
	public Address getEventFiringAddress() { return null; }
}
