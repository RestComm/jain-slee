package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.LinkedList;

import javax.slee.ActivityContextInterface;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;

/**
 * The context of event routing stored in the transaction
 * 
 * @author martins
 * 
 */
public class EventRoutingTransactionData {

	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private static final String TRANSACTION_CONTEXT_KEY = "ertd";

	/**
	 * a linked list with the sbb entities in the call tree, since the event was
	 * passed to the event handler method
	 */
	private final LinkedList<String> invokedSbbEntities = new LinkedList<String>();

	/**
	 * the event being delivered
	 */
	private final DeferredEvent eventBeingDelivered;

	/**
	 * the aci, which is receiving the event
	 */
	private final ActivityContextInterface aciReceivingEvent;

	public EventRoutingTransactionData(DeferredEvent eventBeingDelivered,
			ActivityContextInterface aciReceivingEvent) {
		this.eventBeingDelivered = eventBeingDelivered;
		this.aciReceivingEvent = aciReceivingEvent;
	}

	/**
	 * Retrieves a linked list with the sbb entities in the call tree, since the
	 * event was passed to the event handler method
	 * 
	 * @return
	 */
	public LinkedList<String> getInvokedSbbEntities() {
		return invokedSbbEntities;
	}

	/**
	 * Retrieves the event being delivered
	 * 
	 * @return
	 */
	public DeferredEvent getEventBeingDelivered() {
		return eventBeingDelivered;
	}

	/**
	 * Retrieves the aci, which is receiving the event
	 * 
	 * @return
	 */
	public ActivityContextInterface getAciReceivingEvent() {
		return aciReceivingEvent;
	}
	
	// INTERACTION WITH TX CONTEXT
	
	/**
	 * Puts the data into tx context
	 */
	public void putInTransactionContext() throws TransactionRequiredLocalException {
		try {
			sleeContainer.getTransactionManager().getTransactionContext().getData().put(TRANSACTION_CONTEXT_KEY,this);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}
	
	/**
	 * Retrieves the data stored in active tx context
	 * @return  null if there is no event routing tx data for the active tx
	 */
	public static EventRoutingTransactionData getFromTransactionContext() throws TransactionRequiredLocalException {
		try {
			return (EventRoutingTransactionData) sleeContainer.getTransactionManager().getTransactionContext().getData().get(TRANSACTION_CONTEXT_KEY);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

	public void removeFromTransactionContext() {
		try {
			sleeContainer.getTransactionManager().getTransactionContext().getData().remove(TRANSACTION_CONTEXT_KEY);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

}
