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

package org.mobicents.slee.runtime.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

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
public class TransactionContextImpl implements TransactionContext {

	private static final Logger logger = Logger.getLogger(TransactionContextImpl.class);
	
	/**
	 * {@link TransactionalAction}s which should be executed first after transaction commit succeeds
	 */
	private List<TransactionalAction> afterCommitPriorityActions;

	/**
	 * {@link TransactionalAction}s which should be executed after transaction commit succeeds
	 */
	private List<TransactionalAction> afterCommitActions;

	/**
	 * {@link TransactionalAction}s which should be executed after transaction rollback
	 */
	private List<TransactionalAction> afterRollbackActions;

	/**
	 * {@link TransactionalAction}s which should be executed before transaction commits
	 */
	private List<TransactionalAction> beforeCommitActions;
	
	/**
	 * {@link TransactionalAction}s which should be executed before transaction commits at first
	 */
	private List<TransactionalAction> beforeCommitPriorityActions;

	/**
	 * transaction data
	 */
	@SuppressWarnings("unchecked")
	private Map data;

	/**
	 * place holder for event routing data related with a tx
	 */
	private EventRoutingTransactionData eventRoutingTransactionData;
	
	/**
	 * indicates if the tx context should do traces or not
	 */
	private final static boolean trace = logger.isTraceEnabled();
	
	/**
	 * Retrieves the list of actions which should be executed after commit succeeds 
	 * @return
	 */
	public List<TransactionalAction> getAfterCommitActions() {
		if (afterCommitActions == null) {
			afterCommitActions = new ArrayList<TransactionalAction>();
		}
		return afterCommitActions;
	}

	/**
	 * Retrieves the list of actions which should be executed first after commit succeeds 
	 * @return
	 */
	public List<TransactionalAction> getAfterCommitPriorityActions() {
		if (afterCommitPriorityActions == null) {
			afterCommitPriorityActions = new ArrayList<TransactionalAction>();
		}
		return afterCommitPriorityActions;
	}

	/**
	 * Retrieves the list of actions which should be executed after rollback
	 * @return
	 */
	public List<TransactionalAction> getAfterRollbackActions() {
		if (afterRollbackActions == null) {
			afterRollbackActions = new ArrayList<TransactionalAction>();
		}
		return afterRollbackActions;
	}

	/**
	 * Retrieves the list of actions which should be executed before commit
	 * @return
	 */
	public List<TransactionalAction> getBeforeCommitActions() {
		if (beforeCommitActions == null) {
			beforeCommitActions = new ArrayList<TransactionalAction>();
		}
		return beforeCommitActions;
	}
	
	/**
	 * Retrieves the list of actions which should be executed before commit at first
	 * @return
	 */
	public List<TransactionalAction> getBeforeCommitPriorityActions() {
		if (beforeCommitPriorityActions == null) {
			beforeCommitPriorityActions = new ArrayList<TransactionalAction>();
		}
		return beforeCommitPriorityActions;
	}

	// ------- DATA MANAGEMENT

	@SuppressWarnings("unchecked")
	public Map getData() {
		if (data == null) {
			data = new HashMap();
		}
		return data;
	}

	// ------- ACTIONS EXECUTION

	/**
	 * Executes actions scheduled after commit succeeds
	 */
	protected void executeAfterCommitActions() {
		if (afterCommitActions != null) {
			if (trace) {
				logger.trace("Executing after commit actions");
			}
			executeActions(afterCommitActions,trace);
			afterCommitActions = null;
		} 
	}

	/**
	 * Executes actions scheduled to run first after commit succeeds
	 */
	protected void executeAfterCommitPriorityActions() {
		if (afterCommitPriorityActions != null) {
			if (trace) {
				logger.trace("Executing after commit priority actions");
			}
			executeActions(afterCommitPriorityActions,trace);
			afterCommitPriorityActions = null;
		}
	}

	/**
	 * Executes actions scheduled for after a rollback
	 */
	protected void executeAfterRollbackActions() {		
		if (afterRollbackActions != null) {
			if (trace) {
				logger.trace("Executing rollback actions");
			}
			executeActions(afterRollbackActions,trace);
			afterRollbackActions = null;
		} 
	}

	/**
	 * Executes actions scheduled for before commit
	 */
	protected void executeBeforeCommitActions() {
		if (beforeCommitActions != null) {
			if (trace) {
				logger.trace("Executing before commit actions");
			}
			executeActions(beforeCommitActions,trace);
			beforeCommitActions = null;
		} 
	}
	
	/**
	 * Executes actions scheduled for before commit at first
	 */
	protected void executeBeforeCommitPriorityActions() {		
		if (beforeCommitPriorityActions != null) {
			if (trace) {
				logger.trace("Executing before commit priority actions");
			}
			executeActions(beforeCommitPriorityActions,trace);
			beforeCommitPriorityActions = null;
		} 
	}

	private void executeActions(List<TransactionalAction> actions,boolean trace) {
		for (TransactionalAction action : actions) {
			if (trace)
				logger.trace("Executing action:" + action);
			action.execute();
		}
	}

	/**
	 * Cleanups any state the entry has created.
	 */
	protected void cleanup() {
		data = null;
		eventRoutingTransactionData = null;
	}

	public EventRoutingTransactionData getEventRoutingTransactionData() {
		return eventRoutingTransactionData;
	}

	public void setEventRoutingTransactionData(EventRoutingTransactionData eventRoutingTransactionData) {
		this.eventRoutingTransactionData = eventRoutingTransactionData;
	}
	
}