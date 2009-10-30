package org.mobicents.slee.runtime.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private static final Logger logger = Logger
			.getLogger(SleeTransactionManagerImpl.class);
	
	/**
	 * the underlying JTA tx manager
	 */
	private final TransactionManager transactionManager;
	
	/**
	 * an executor service for async operations invoked on {@link SleeTransaction}
	 */
	private ExecutorService executorService;
	
	/**
	 * 
	 * @param transactionManager
	 */
	public SleeTransactionManagerImpl(TransactionManager transactionManager) {		
		this.transactionManager = transactionManager;
		logger.info("SLEE Transaction Manager created.");
	}

	/**
	 * 
	 * @return
	 */
	ExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#getRealTransactionManager()
	 */
	public TransactionManager getRealTransactionManager() {
		return transactionManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#getRollbackOnly()
	 */
	public boolean getRollbackOnly() throws SystemException {
		return getStatus() == Status.STATUS_MARKED_ROLLBACK;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#mandateTransaction()
	 */
	public void mandateTransaction() throws TransactionRequiredLocalException {
		try {
			final Transaction tx = getTransaction();
			if (tx == null)
				throw new TransactionRequiredLocalException(
						"Transaction Mandatory");
			final int status = tx.getStatus();
			if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {
				throw new IllegalStateException(
						"There is no active tx, tx is in state: "
						+ status);
			}
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#requireTransaction()
	 */
	public boolean requireTransaction() {
		
		try {
			final Transaction tx = getTransaction();
			if (tx == null) {
				begin();
				return true;
			}
			else {
				final int status = tx.getStatus();
				if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {
					begin();
					return true;
				}		
			}						
		} catch (NotSupportedException e) {
			logger.error("Exception creating transaction", e);
		} catch (SystemException e) {
			logger.error("Caught SystemException in checking transaction", e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#requireTransactionEnd(boolean, boolean)
	 */
	public void requireTransactionEnd(boolean terminateTx, boolean doRollback) throws IllegalStateException, SecurityException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		if (terminateTx) {
			if (doRollback) {
				rollback();
			}
			else {
				commit();
			}
		}
		else {
			if (doRollback) {
				setRollbackOnly();	
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransactionManager#asyncCommit(javax.slee.transaction.CommitListener)
	 */
	public void asyncCommit(CommitListener commitListener) throws IllegalStateException,
			SecurityException {
		try {
			final SleeTransaction sleeTransaction = getSleeTransaction();
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

	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransactionManager#asyncRollback(javax.slee.transaction.RollbackListener)
	 */
	public void asyncRollback(RollbackListener rollbackListener)
			throws IllegalStateException, SecurityException {
		try {
			final SleeTransaction sleeTransaction = getSleeTransaction();
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

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#begin()
	 */
	public void begin() throws NotSupportedException, SystemException {
		// begin transaction
		transactionManager.begin();
		// get tx does the rest
		getAsSleeTransaction(transactionManager.getTransaction(),true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransactionManager#beginSleeTransaction()
	 */
	public SleeTransaction beginSleeTransaction() throws NotSupportedException,
			SystemException {
		// begin transaction
		transactionManager.begin();
		// get tx does the rest
		return getAsSleeTransaction(transactionManager.getTransaction(),true);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#getTransaction()
	 */
	public Transaction getTransaction() throws SystemException {
		return getAsSleeTransaction(transactionManager.getTransaction(),false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransactionManager#getSleeTransaction()
	 */
	public SleeTransaction getSleeTransaction() throws SystemException {
		return getAsSleeTransaction(transactionManager.getTransaction(),false);		
	}

	private SleeTransaction getAsSleeTransaction(Transaction transaction, boolean transactionCreation) throws SystemException {
		if (transaction != null) {
			TransactionContext transactionContext = null;
			if (transactionCreation) {
				transactionContext = bindToTransaction(transaction);
				if (logger.isDebugEnabled()) {
					logger.debug("Started tx "+transaction);
				}
			}
			else {
				transactionContext = TransactionContextThreadLocal.getTransactionContext();
				if (transactionContext == null && transaction.getStatus() == Status.STATUS_ACTIVE) {
					// we need to bind to tx, this is edge case for a tx that was created directly in the underlying tx manager
					transactionContext = bindToTransaction(transaction);
				}
			}
			return new SleeTransactionImpl((TransactionImple)transaction,transactionContext,this);
		}
		else {
			return null;
		}		
	}

	private TransactionContext bindToTransaction(Transaction tx) throws IllegalStateException, SystemException {
		final TransactionContext txContext = new TransactionContext();
		// register for call-backs
		try {
			tx.registerSynchronization(new SleeTransactionSynchronization(tx,txContext));
		} catch (RollbackException e) {
			throw new IllegalStateException("Unable to register listener for created transaction. Error: "+e.getMessage());
		}
		// store tx context in thread
		TransactionContextThreadLocal.setTransactionContext(txContext);
		return txContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransactionManager#asSleeTransaction(javax.transaction.Transaction)
	 */
	public SleeTransaction asSleeTransaction(Transaction transaction)
			throws NullPointerException, IllegalArgumentException,
			SystemException {
		if (transaction == null) {
			throw new NullPointerException("null transaction");
		}
		if (transaction.getClass() == SleeTransactionImpl.class) {
			return (SleeTransaction) transaction;
		}
		if (transaction instanceof TransactionImple) {
			return new SleeTransactionImpl((TransactionImple) transaction,getTransactionContext(),this);
		}
		throw new IllegalArgumentException("unexpected transaction class type "+transaction.getClass());
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#commit()
	 */
	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting commit of tx "+transactionManager.getTransaction());
		}	
		transactionManager.commit();		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#getStatus()
	 */
	public int getStatus() throws SystemException {
		return transactionManager.getStatus();				
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#resume(javax.transaction.Transaction)
	 */
	public void resume(Transaction transaction) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		if (transaction.getClass() == SleeTransactionImpl.class) {
			final SleeTransactionImpl sleeTransactionImpl = (SleeTransactionImpl) transaction;
			if (logger.isDebugEnabled()) {
				logger.debug("Resuming tx "+sleeTransactionImpl.getWrappedTransaction());
			}
			// resume wrapped tx
			transactionManager.resume(sleeTransactionImpl.getWrappedTransaction());
			// store tx context in thread 
			TransactionContextThreadLocal.setTransactionContext(sleeTransactionImpl.getTransactionContext());
		}
		else {
			throw new InvalidTransactionException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#rollback()
	 */
	public void rollback() throws IllegalStateException, SecurityException,
			SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting rollback of tx "+transactionManager.getTransaction());
		}				
		transactionManager.rollback();			
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#setRollbackOnly()
	 */
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Marking tx "
					+ transactionManager.getTransaction()+" for rollback.");
		}
		transactionManager.setRollbackOnly();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#setTransactionTimeout(int)
	 */
	public void setTransactionTimeout(int seconds) throws SystemException {
		transactionManager.setTransactionTimeout(seconds);			
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.TransactionManager#suspend()
	 */
	public Transaction suspend() throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Suspending tx "+transactionManager.getTransaction());
		}
		final Transaction tx = getAsSleeTransaction(transactionManager.suspend(),false);
		if (tx != null) {
			// remove tx context from thread
			TransactionContextThreadLocal.setTransactionContext(null);
			return tx;
		}
		else {
			return null;
		}
	}
	
	// --- TX CONTEXT AND ACTION METHODS
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.SleeTransactionManager#getTransactionContext()
	 */
	public TransactionContext getTransactionContext() {
		TransactionContext txContext = TransactionContextThreadLocal.getTransactionContext();
		if (txContext == null) {
			try {
				final Transaction tx = transactionManager.getTransaction();
				if (tx != null && tx.getStatus() == Status.STATUS_ACTIVE) {
					// a tx was started with the real tx manager, lets try to hook the sync handler and a new tx context
					txContext = bindToTransaction(tx);
				}
			}
			catch (Throwable e) {
				throw new SLEEException(e.getMessage(), e);
			}
		}
		return txContext;
	}
	
}