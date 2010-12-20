package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * The context of event routing stored in the transaction
 * 
 * @author martins
 * 
 */
public class EventRoutingTransactionDataImpl implements EventRoutingTransactionData {
	
	/**
	 * a linked list with the non reentrant sbb entities in the call tree, since the event was
	 * passed to the event handler method
	 */
	private Set<SbbEntityID> invokedSbbEntities = null;

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
	 * Retrieves a set with the non reentrant sbb entities in the call tree, since the
	 * event was passed to the event handler method
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getInvokedNonReentrantSbbEntities() {
		if (invokedSbbEntities == null) {
			invokedSbbEntities = new HashSet<SbbEntityID>();
		}
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
