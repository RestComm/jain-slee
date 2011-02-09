/**
 * 
 */
package org.mobicents.slee.container.activity;

import javax.transaction.Transaction;

import org.mobicents.slee.container.event.EventContext;

/**
 * @author martins
 *
 */
public interface ActivityEventQueueManager {

	/**
	 * Indicates if there are pending events to be routed for the related
	 * activity.
	 * 
	 * @return
	 */
	public boolean noPendingEvents();

	/**
	 * Defines that the specified event is now pending
	 * 
	 * @param ef
	 */
	public void pending(EventContext ef);
	
	/**
	 * Signals the manager that the event was committed, and thus can be routed.
	 * 
	 * @param ef
	 */
	public void commit(EventContext ef);
	
	/**
	 * Similar as doing pending() and commit() of an event in a single step.
	 * @param ef
	 */
	public void fireNotTransacted(EventContext ef);
	
	/**
	 * Signals that the java transaction who fired the specified event did not
	 * commit, and thus the event should be not routed.
	 * 
	 * @param ef
	 */
	public void rollback(EventContext ef);
	
	/**
	 * create a barrier for the specified transaction for this activity event
	 * queue, events committed are frozen till all barriers are removed
	 */
	public void createBarrier(Transaction transaction);
	
	/**
	 * remove a barrier for the specified transaction for this activity event
	 * queue, if there are no more barriers then delivering of events frozen proceed
	 */
	public void removeBarrier(Transaction transaction);

}
