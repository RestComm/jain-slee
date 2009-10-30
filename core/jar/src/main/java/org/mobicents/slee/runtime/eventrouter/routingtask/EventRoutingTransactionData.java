package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.LinkedList;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.transaction.TransactionContext;

/**
 * The context of event routing stored in the transaction
 * 
 * @author martins
 * 
 */
public class EventRoutingTransactionData {

	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
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
	
}
