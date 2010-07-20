package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;

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
	 * @throws SLEEException
	 */
	void execute(final ActivityHandle handle, final int activityFlags)
			throws SLEEException {

		final Transaction tx = super.suspendTransaction();
		ActivityContextHandle ach = null;
		try {
			ach = sleeEndpoint._startActivity(handle, activityFlags);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
				// the activity was started out of the tx but it will be suspended, if the flags request the unreferenced callback then
				// we can schedule a check for references in the end of the tx, this ensures that the callback is received if no events are fired or 
				// events are fired but not handled, that is, no reference is ever ever created
				if (ach != null && ActivityFlags.hasRequestSleeActivityGCCallback(activityFlags)) {
					final ActivityContext ac = acFactory.getActivityContext(ach);
					if (ac != null) {
						ac.scheduleCheckForUnreferencedActivity();
					}
				}
			}
		}
	}
}
