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

		final Transaction tx = super.suspendTransaction();
		try {
			sleeEndpoint._endActivity(handle);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
