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

package org.mobicents.slee.container.transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

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
	 * @return
	 */
	@SuppressWarnings("rawtypes")
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
	
	/**
	 * Retrieves a set with the non reentrant sbb entities in the call tree, since the
	 * event was passed to the event handler method
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getInvokedNonReentrantSbbEntities();
	
}