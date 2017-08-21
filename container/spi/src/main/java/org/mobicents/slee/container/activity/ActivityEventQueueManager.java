/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
