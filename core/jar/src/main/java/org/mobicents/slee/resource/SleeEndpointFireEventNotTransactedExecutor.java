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
	 * @param handle
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
	void execute(final ActivityHandle handle,
			final FireableEventType eventType, final Object event,
			final Address address, final ReceivableService receivableService,
			final int eventFlags) throws ActivityIsEndingException,
			FireEventException, SLEEException,
			UnrecognizedActivityHandleException {
		
		final Transaction tx = super.suspendTransaction();
		try {
			sleeEndpoint._fireEvent(handle, eventType, event, address,
					receivableService, eventFlags);
		} finally {
			if (tx != null) {
				super.resumeTransaction(tx);
			}
		}
	}
}
