/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.TransactionRequiredLocalException;
import javax.slee.transaction.SleeTransaction;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;

/**
 * Implementation of SLEE Tx manager.
 * 
 * @author Tim Fox - Complete re-write.
 * @author M. Ranganathan
 * @author J. Deruelle
 * @author Ralf Siedow
 * @author Ivelin Ivanov
 * @author Eduardo Martins version 2
 * 
 */
public class SleeTransactionManagerImpl implements SleeTransactionManager {

	private static Logger logger = Logger
			.getLogger(SleeTransactionManagerImpl.class);

	private ConcurrentHashMap<Transaction, TransactionContext> transactionContexts = new ConcurrentHashMap<Transaction, TransactionContext>();

	private final TransactionManager transactionManager;
	
	public SleeTransactionManagerImpl(TransactionManager transactionManager) {		
		this.transactionManager = transactionManager;
		logger.info("SLEE Transaction Manager created.");
	}
	
	public void begin() throws SystemException {
		
		Transaction tx = getTransaction();
		
		// check there is no transaction
		if (tx != null) {
			throw new SystemException("Transaction already started, cannot nest tx. Ongoing Tx: "+ tx);			
		} 
		
		// begin transaction
		try {
			transactionManager.begin();
		} catch (NotSupportedException e) {				
			throw new SystemException("Failed to begin transaction." + e);
		}
		
		tx = getTransaction();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Transaction started: "+tx);
		}	

		// register for call-backs
		try {
			tx.registerSynchronization(new SynchronizationHandler(tx));
		} catch (RollbackException e) {
			throw new SystemException("Unable to register listener for created transaction. Error: "+e.getMessage());
		}
		
	}

	public void commit() throws SystemException {
		
		Transaction tx = getTransaction();

		if (tx == null) {
			throw new SystemException(
					"Failed to commit transaction since there is no transaction to commit!");
		}

		if (getRollbackOnly()) {
			if (logger.isDebugEnabled())
				logger
						.debug("Transaction marked for roll back, cannot commit, ending with rollback: "
								+ tx);
			rollback();
		} else if (tx.getStatus() == Status.STATUS_ACTIVE) {
			try {
				transactionManager.commit();
				if (logger.isDebugEnabled())
					logger.debug("Committed tx "+tx);
			} catch (Exception e) {
				throw new SystemException("Failed to commit tx " + tx + ". Error: "
						+ e.getMessage());
			}
		} else {
			throw new SystemException(
					"Failed to commit transaction "+tx+" since state is "
							+ tx.getStatus());
		}
	}


	public boolean getRollbackOnly() throws SystemException {
		
		Transaction tx = getTransaction();
		
		if (tx == null) {
			throw new SystemException("no transaction");
		}
		return tx.getStatus() == Status.STATUS_MARKED_ROLLBACK;
		// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
		//return tx.getStatus() == Status.STATUS_MARKED_ROLLBACK || (tx.getStatus() == Status.STATUS_ACTIVE && getTransactionContext().getRollbackOnly());
	}

	public void setRollbackOnly() throws SystemException {
		
		if (transactionManager != null) {
			
			transactionManager.setRollbackOnly();
			// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
			//getTransactionContext().setRollbackOnly();
			
			if (logger.isDebugEnabled())
				logger.debug("rollbackonly set on tx "
						+ getTransaction());
		}
		else {
			throw new SystemException("tx manager unavailable");
		}			
	}
	
	public void rollback() throws SystemException {
		
		Transaction tx = getTransaction();
		
		if (tx == null) {
			throw new SystemException(
					"Failed to rollback transaction since there is no transaction to rollback!");
		}

		if (tx.getStatus() != Status.STATUS_ACTIVE && tx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
			throw new SystemException(
					"Failed to rollback transaction since transaction is in state: "
							+ tx.getStatus());
		}

		transactionManager.rollback();

		if (logger.isDebugEnabled()) {
			logger.debug("Rollbacked tx "+tx);
		}
	}

	public Transaction getTransaction() {
		
		if (transactionManager != null) {
			try {
				return transactionManager.getTransaction();
			} catch (SystemException e) {
				throw new RuntimeException(
						"Failed to obtain active JTA transaction");
			}
		}
		else {
			throw new RuntimeException("tx manager unavailable");
		}
	}

	public boolean isInTx() throws SystemException {
		
		return getTransaction() != null;
	}

	public void mandateTransaction() throws TransactionRequiredLocalException {
		
		try {
			Transaction tx = getTransaction();

			if (logger.isDebugEnabled()) {
				logger.debug("mandateTransaction() invoked. tx = "+tx);
			}
			
			if (tx == null)
				throw new TransactionRequiredLocalException(
						"Transaction Mandatory");

			if (tx.getStatus() != Status.STATUS_ACTIVE && tx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
				throw new IllegalStateException(
						"There is no active tx, tx is in state: "
								+ tx.getStatus());
			}
		} catch (SystemException e) {
			logger.error(
					"Caught SystemException in getting transaction/ status", e);
		}		
	}

	public boolean requireTransaction() {
		
		try {
			Transaction tx = getTransaction();

			if (tx == null) {
				// No transaction so start a new one
				if (logger.isDebugEnabled()) {
					logger.debug("requireTransaction: no tx, creating one");
				}
				this.begin();
				return true;
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("requireTransaction: tx already exists");
			}
			
			if (tx.getStatus() != Status.STATUS_ACTIVE && tx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
				throw new IllegalStateException(
						"Transaction is in illegal state: " + tx.getStatus());
			}
		} catch (SystemException e) {
			logger.error("Caught SystemException in checking transaction", e);
		}
		return false;
	}

	

	public int getStatus() throws SystemException {
		
		Transaction tx = getTransaction();

		if (tx == null)
			throw new SystemException("no transaction");

		return tx.getStatus();
		
		// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
		/*
		int status = tx.getStatus();
		if (status == Status.STATUS_ACTIVE && getTransactionContext().getRollbackOnly()) {
			return Status.STATUS_MARKED_ROLLBACK;
		}
		else {
			return status;
		}
		*/
	}

	public void resume(Transaction txToResume) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		
		if (transactionManager != null) {
			transactionManager.resume(txToResume);	
		}
		else {
			throw new SystemException("tx manager unavailable");
		}				
	}

	public void setTransactionTimeout(int timeout) throws SystemException {
		
		if (transactionManager != null) {
			transactionManager.setTransactionTimeout(timeout);	
		}
		else {
			throw new SystemException("tx manager unavailable");
		}		
	}

	public Transaction suspend() throws SystemException {
		
		if (transactionManager != null) {
			return transactionManager.suspend();
		}
		else {
			throw new SystemException("tx manager unavailable");
		}		
	}
	
	public String displayOngoingSleeTransactions() {

		if (logger.isDebugEnabled()) {		
			String msg = "---------+ Begin dump of SLEE TX map: +--------------------------";
			for (Transaction tx : transactionContexts.keySet()) {							
				msg += "\n----+ Transaction: " + tx;
			}
			msg += "\nEnd dump of SLEE TX map: -------------------------- \n";			
			return msg;			
		} else {			
			return "";			
		}
	}

	// --- TX CONTEXT AND ACTION METHODS
	
	public TransactionContext getTransactionContext() throws SystemException {
		
		Transaction tx = getTransaction();
		
		if (tx == null) {
			throw new SystemException("no transaction");
		}
		
		TransactionContext tc = transactionContexts.get(tx);
		if (tc == null) {
			tc = new TransactionContext();
			transactionContexts.put(tx, tc);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("getTransactionContext() invoked for tx "+tx+" . Transaction context is "+tc);
		}
		return tc;
	}

	public void addAfterCommitAction(TransactionalAction action)
			throws SystemException {

		getTransactionContext().getAfterCommitActions().add(action);
	}
	
	public void addAfterCommitPriorityAction(TransactionalAction action)
			throws SystemException {
		
		getTransactionContext().getAfterCommitPriorityActions().add(action);
	}
	
	public void addAfterRollbackAction(TransactionalAction action) {
		try {
			Transaction tx = getTransaction();
			if (tx != null) {
				getTransactionContext().getAfterRollbackActions().add(action);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public void addBeforeCommitAction(TransactionalAction action)
			throws SystemException {
		
		getTransactionContext().getBeforeCommitActions().add(action);
	}
	
	// --- NOT IMPLEMENTED
	
	public void asyncCommit() throws IllegalStateException, SecurityException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException {
		throw new UnsupportedOperationException();		
	}

	public void asyncRollback() throws IllegalStateException,
			SecurityException, SystemException {
		throw new UnsupportedOperationException();		
	}

	public SleeTransaction beginSleeTransaction() throws NotSupportedException,
			SystemException {
		throw new UnsupportedOperationException();
	}

	
	// --- TX MANAGER LISTENER
	
	class SynchronizationHandler implements Synchronization {
		private Transaction tx;

		public SynchronizationHandler(Transaction tx) {
			this.tx = tx;
		}

		public void afterCompletion(int status) {

			// get tx context
			TransactionContext txContext = transactionContexts.remove(tx);
			if (txContext != null) {
				switch (status) {

				case Status.STATUS_COMMITTED:
					if (logger.isDebugEnabled()) {
						logger.debug("Transaction committed: " + tx);
					}
					txContext.executeAfterCommitPriorityActions();
					txContext.executeAfterCommitActions();
					break;

				case Status.STATUS_ROLLEDBACK:
					if (logger.isDebugEnabled()) {
						logger.debug("Transaction rollback: " + tx);
					}
					txContext.executeAfterRollbackActions();
					break;

				default:				
					throw new IllegalStateException("Unexpected transaction state "
							+ status);
				}
				txContext.cleanup();
			}
		}

		public void beforeCompletion() {
			if (logger.isDebugEnabled())
				logger.debug("beforeCompletion for tx " + tx);
			// get entry and execute before commit actions
			TransactionContext tc = transactionContexts.get(tx);
			if (tc != null) {
				tc.executeBeforeCommitActions();
			}
		}
	}
	
	@Override
	public String toString() {
		return "SLEE Transaction Manager: " 
			+ "\n+-- Number of transactions: " + transactionContexts.size();
	}
}
