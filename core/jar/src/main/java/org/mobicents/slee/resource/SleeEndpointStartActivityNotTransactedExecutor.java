package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.StartActivityException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 *
 */
public class SleeEndpointStartActivityNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointStartActivityNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted start activity operation.
	 * @param handle
	 * @param activityFlags
	 * @throws StartActivityException
	 * @throws SLEEException
	 */
	void execute(final ActivityHandle handle, final int activityFlags)
			throws StartActivityException, SLEEException {

		final Transaction tx = super.suspendTransaction();
		try {
			sleeEndpoint._startActivity(handle, activityFlags);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
