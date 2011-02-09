package org.mobicents.slee.resource;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 *
 */
public class SleeEndpointFireEventNotTransactedExecutor extends
		SleeEndpointOperationNotTransactedExecutor {

	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	public SleeEndpointFireEventNotTransactedExecutor(
			SleeContainer sleeContainer, SleeEndpointImpl sleeEndpoint) {
		super(sleeContainer, sleeEndpoint);
	}

	/**
	 * Executes a non transacted fire event operation.
	 * 
	 * @param realHandle
	 * @param refHandle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @throws ActivityIsEndingException
	 * @throws FireEventException
	 * @throws SLEEException
	 * @throws UnrecognizedActivityHandleException
	 */
	void execute(final ActivityHandle realHandle, final ActivityHandle refHandle,
			final FireableEventType eventType, final Object event,
			final Address address, final ReceivableService receivableService,
			final int eventFlags) throws ActivityIsEndingException,
			FireEventException, SLEEException,
			UnrecognizedActivityHandleException {
		
		// suspend the tx and activity if there is a tx
		// suspend also the activity to block the event
		// till the tx ends (may have pending changes to the activity context)
		final Transaction tx = super.suspendTransactionAndActivity(refHandle);
		try {
			sleeEndpoint._fireEvent(realHandle, refHandle, eventType, event, address,
					receivableService, eventFlags);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
