package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.LinkedList;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;

/**
 * The context of event routing stored in the transaction
 * 
 * @author martins
 * 
 */
public class EventRoutingTransactionDataImpl implements EventRoutingTransactionData {
	
	/**
	 * a linked list with the sbb entities in the call tree, since the event was
	 * passed to the event handler method
	 */
	private final LinkedList<String> invokedSbbEntities = new LinkedList<String>();

	/**
	 * the event being delivered
	 */
	private final EventContext eventBeingDelivered;

	/**
	 * the aci, which is receiving the event
	 */
	private final ActivityContextInterface aciReceivingEvent;

	public EventRoutingTransactionDataImpl(EventContext eventBeingDelivered,
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
	public EventContext getEventBeingDelivered() {
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
