/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-28                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
public class TransactionContext {

	private static Logger logger = Logger.getLogger(TransactionContext.class);

	// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
	//private boolean rollbackOnly = false;
	
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
	private Map data;

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
			if (logger.isDebugEnabled()) {
				logger.debug("Executing after commit actions");
			}
			executeActions(afterCommitActions);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No after commit actions to execute");
			}
		}
	}

	/**
	 * Executes actions scheduled to run first after commit succeeds
	 */
	protected void executeAfterCommitPriorityActions() {

		if (afterCommitPriorityActions != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing after commit priority actions");
			}
			executeActions(afterCommitPriorityActions);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No after commit priority actions to execute");
			}
		}
	}

	/**
	 * Executes actions scheduled for after a rollback
	 */
	protected void executeAfterRollbackActions() {
		
		if (afterRollbackActions != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing rollback actions");
			}
			executeActions(afterRollbackActions);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No rollback actions to execute");
			}
		}
	}

	/**
	 * Executes actions scheduled for before commit
	 */
	protected void executeBeforeCommitActions() {
		
		if (beforeCommitActions != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing before commit actions");
			}
			executeActions(beforeCommitActions);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No before commit actions to execute");
			}
		}
	}
	
	/**
	 * Executes actions scheduled for before commit at first
	 */
	protected void executeBeforeCommitPriorityActions() {
		
		if (beforeCommitPriorityActions != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing before commit priority actions");
			}
			executeActions(beforeCommitPriorityActions);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No before commit priority actions to execute");
			}
		}
	}

	private void executeActions(List<TransactionalAction> actions) {
		
		for (TransactionalAction action : actions) {
			if (logger.isDebugEnabled())
				logger.debug("Executing action:" + action);
			try {
				action.execute();
			} catch (Throwable t) {
				throw new RuntimeException("Failed while executing action", t);
			}
		}
	}

	/**
	 * Cleanups any state the entry has created.
	 */
	protected void cleanup() {
		afterCommitActions = null;
		afterCommitPriorityActions = null;
		afterRollbackActions = null;
		beforeCommitActions = null;
		beforeCommitPriorityActions = null;
		data = null;
	}
	
	// this code was hack to trap setRollbackOnly() in the tx context, due to jboss cache, don't remove it may be needed again
	/*
	protected boolean getRollbackOnly() {
		
		return rollbackOnly;
	}
	
	protected void setRollbackOnly() {
		
		rollbackOnly = true;;
	}
	*/
	
}