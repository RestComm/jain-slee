/**
 * 
 */
package org.mobicents.slee.container.eventrouter;

import java.util.Set;

import javax.slee.ActivityContextInterface;

import org.mobicents.slee.container.event.EventContext;

/**
 * The context of event routing stored in the transaction.
 * 
 * @author martins
 * 
 */
public interface EventRoutingTransactionData {

	/**
	 * Retrieves the aci, which is receiving the event
	 * 
	 * @return
	 */
	public ActivityContextInterface getAciReceivingEvent();

	/**
	 * Retrieves the event being delivered
	 * 
	 * @return
	 */
	public EventContext getEventBeingDelivered();

	/**
	 * Retrieves a set with the non reentrant sbb entities in the call tree, since the
	 * event was passed to the event handler method
	 * 
	 * @return
	 */
	public Set<String> getInvokedNonReentrantSbbEntities();
}
