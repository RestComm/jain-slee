package org.mobicents.slee.resource;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 * 
 */
public class SleeEndpointEndActivityNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointEndActivityNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted End Activity operation.
	 *  
	 * @param handle
	 * @throws UnrecognizedActivityHandleException
	 */
	void execute(final ActivityHandle handle)
			throws UnrecognizedActivityHandleException {

		// suspend the tx and activity if there is a tx
		// suspend also the activity to block the activity end event
		// till the tx ends (may have pending changes to the activity context)
		final Transaction tx = super.suspendTransactionAndActivity(handle);
		try {
			sleeEndpoint._endActivity(handle);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
