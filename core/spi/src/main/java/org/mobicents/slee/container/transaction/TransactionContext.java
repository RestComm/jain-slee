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

package org.mobicents.slee.container.transaction;

import java.util.List;
import java.util.Map;

import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;

/**
 * 
 * The local context of a transaction. 
 * 
 * Provides a {@link Map} to store data in the transaction.
 * 
 * Provides various {@link List}s for {@link TransactionalAction}s to be added. Those lists can be:
 * 
 *   + Before Commit Action, an action to execute before the transaction is committed
 *   
 *   + After Commit Priority Action, an action to execute first after the transaction is committed
 *   
 *   + After Commit Action, an action to execute after the transaction is committed and the priority actions execution
 *   
 *   + After Rollback Action, an action to execute after the transaction rollbacks 
 * 
 * @author ? 
 * @author martins
 *
 */
public interface TransactionContext {
	
	/**
	 * Retrieves the list of actions which should be executed after commit succeeds 
	 * @return
	 */
	public List<TransactionalAction> getAfterCommitActions();

	/**
	 * Retrieves the list of actions which should be executed first after commit succeeds 
	 * @return
	 */
	public List<TransactionalAction> getAfterCommitPriorityActions();

	/**
	 * Retrieves the list of actions which should be executed after rollback
	 * @return
	 */
	public List<TransactionalAction> getAfterRollbackActions();

	/**
	 * Retrieves the list of actions which should be executed before commit
	 * @return
	 */
	public List<TransactionalAction> getBeforeCommitActions();
	
	/**
	 * Retrieves the list of actions which should be executed before commit at first
	 * @return
	 */
	public List<TransactionalAction> getBeforeCommitPriorityActions();

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map getData();

	/**
	 * 
	 * @return
	 */
	public EventRoutingTransactionData getEventRoutingTransactionData();

	/**
	 * 
	 * @param eventRoutingTransactionData
	 */
	public void setEventRoutingTransactionData(EventRoutingTransactionData eventRoutingTransactionData);
	
}