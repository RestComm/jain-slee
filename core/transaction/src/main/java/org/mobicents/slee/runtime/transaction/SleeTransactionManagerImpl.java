/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.transaction.CommitListener;
import javax.slee.transaction.RollbackListener;
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

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

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
	
	/**
	 * transaction context per transaction map
	 */
	private ConcurrentHashMap<Transaction, TransactionContext> transactionContexts = new ConcurrentHashMap<Transaction, TransactionContext>();

	/**
	 * the underlying JTA tx manager
	 */
	private final TransactionManager transactionManager;
	
	public SleeTransactionManagerImpl(TransactionManager transactionManager) {		
		this.transactionManager = transactionManager;
		logger.info("SLEE Transaction Manager created.");
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

	public boolean getRollbackOnly() throws SystemException {
		Transaction tx = getTransaction();
		if (tx == null) {
			throw new SystemException("no transaction");
		}
		return tx.getStatus() == Status.STATUS_MARKED_ROLLBACK;
		// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
		//return tx.getStatus() == Status.STATUS_MARKED_ROLLBACK || (tx.getStatus() == Status.STATUS_ACTIVE && getTransactionContext().getRollbackOnly());
	}

	public void mandateTransaction() throws TransactionRequiredLocalException {
		
		Transaction tx = null;
		try {
			tx = getTransaction();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		if (tx == null)
			throw new TransactionRequiredLocalException(
					"Transaction Mandatory");
		int status = -1;
		try {
			status = tx.getStatus();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {
			throw new IllegalStateException(
					"There is no active tx, tx is in state: "
					+ status);
		}

	}

	public boolean requireTransaction() {
		try {
			Transaction tx = getTransaction();
			int status = -1;
			if (tx != null) {
				status = tx.getStatus();
			}
			if (tx == null || (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK)) {
				// start a new one
				if (logger.isDebugEnabled()) {
					logger.debug("requireTransaction: no tx or tx is inactive, creating one");
				}
				this.begin();
				return true;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("requireTransaction: active tx already exists");
			}						
		} catch (NotSupportedException e) {
			logger.error("Exception creating transaction", e);
		} catch (SystemException e) {
			logger.error("Caught SystemException in checking transaction", e);
		}
		return false;
	}

	public void requireTransactionEnd(boolean terminateTx, boolean doRollback) throws SLEEException {
		if (terminateTx) {
			if (doRollback) {
				try {
					rollback();
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
			else {
				try {
					commit();
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
		}
		else {
			if (doRollback) {
				try {
					setRollbackOnly();	
				} catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
		}
	}
	
	public SleeTransaction asSleeTransaction(Transaction transaction)
			throws NullPointerException, IllegalArgumentException,
			SystemException {
		if (transaction == null) {
			throw new NullPointerException("null transaction");
		}
		if (transaction instanceof SleeTransactionImpl) {
			return (SleeTransaction) transaction;
		}
		if (transaction instanceof TransactionImple) {
			return new SleeTransactionImpl((TransactionImple) transaction,this);
		}
		throw new IllegalArgumentException("unexpected transaction class type "+transaction.getClass());
	}
	
	public void asyncCommit(CommitListener commitListener) throws IllegalStateException,
			SecurityException {
		try {
			SleeTransaction sleeTransaction = getSleeTransaction();
			if (sleeTransaction == null) {
				throw new IllegalStateException("no transaction");
			}
			else {
				sleeTransaction.asyncCommit(commitListener);
			}
		} catch (SystemException e) {
			if (commitListener != null) {
				commitListener.systemException(e);
			}
		}		
	}

	public void asyncRollback(RollbackListener rollbackListener)
			throws IllegalStateException, SecurityException {
		try {
			SleeTransaction sleeTransaction = getSleeTransaction();
			if (sleeTransaction == null) {
				throw new IllegalStateException("no transaction");
			}
			else {
				sleeTransaction.asyncRollback(rollbackListener);
			}			
		} catch (SystemException e) {
			if (rollbackListener != null) {
				rollbackListener.systemException(e);
			}
		}
	}

	public SleeTransaction beginSleeTransaction() throws NotSupportedException,
			SystemException {
		begin();
		return getSleeTransaction();
	}

	public SleeTransaction getSleeTransaction() throws SystemException {
		Transaction transaction = getTransaction();
		if (transaction != null) {
			return new SleeTransactionImpl((TransactionImple)transaction,this);
		}
		else {
			return null;
		}		
	}

	public void begin() throws NotSupportedException, SystemException {
		// begin transaction
		transactionManager.begin();
		Transaction tx = getTransaction();
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

	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting commit of tx "+getTransaction());
		}	
		
		transactionManager.commit();		
	}

	public int getStatus() throws SystemException {
		int status = transactionManager.getStatus();				
		// this code was hack to trap setRollbackOnly() due to jboss cache, don't remove it may be needed again
		/*
		if (status == Status.STATUS_ACTIVE && getTransactionContext().getRollbackOnly()) {
			return Status.STATUS_MARKED_ROLLBACK;
		}
		else {
			return status;
		}
		*/
		return status;
	}

	public Transaction getTransaction() throws SystemException {
		if (transactionManager != null) {
			return transactionManager.getTransaction();
		}
		else {
			throw new SystemException("tx manager unavailable");
		}
	}

	public void resume(Transaction transaction) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		if (transactionManager != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Resuming tx "+transaction);
			}
			transactionManager.resume(transaction);	
		}
		else {
			throw new SystemException("tx manager unavailable");
		}	
	}

	public void rollback() throws IllegalStateException, SecurityException,
			SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting rollback of tx "+getTransaction());
		}				
		transactionManager.rollback();			
	}

	public void setRollbackOnly() throws IllegalStateException, SystemException {
		if (transactionManager != null) {
			transactionManager.setRollbackOnly();
			if (logger.isDebugEnabled()) {
				logger.debug("rollbackonly set on tx "
						+ getTransaction());
			}
		}
		else {
			throw new SystemException("tx manager unavailable");
		}
	}

	public void setTransactionTimeout(int seconds) throws SystemException {
		if (transactionManager != null) {
			transactionManager.setTransactionTimeout(seconds);	
		}
		else {
			throw new SystemException("tx manager unavailable");
		}			
	}

	public Transaction suspend() throws SystemException {
		if (transactionManager != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Suspending tx "+getTransaction());
			}
			return transactionManager.suspend();
		}
		else {
			throw new SystemException("tx manager unavailable");
		}		
	}
	
	@Override
	public String toString() {
		return "SLEE Transaction Manager: " 
			+ "\n+-- Number of transactions: " + transactionContexts.size();
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
	
	public void addAfterRollbackAction(TransactionalAction action) throws SystemException {
		getTransactionContext().getAfterRollbackActions().add(action);
	}
	
	public void addBeforeCommitAction(TransactionalAction action)
			throws SystemException {
		getTransactionContext().getBeforeCommitActions().add(action);
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
			// get entry and execute before commit actions
			TransactionContext tc = transactionContexts.get(tx);
			if (tc != null) {
				tc.executeBeforeCommitActions();
			}
		}
	}
	
}
