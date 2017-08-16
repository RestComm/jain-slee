/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
