package org.mobicents.slee.runtime.transaction;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;

/**
 * 
 * @author martins
 *
 */
public class SleeTransactionSynchronization implements Synchronization {

	private static final Logger logger = Logger.getLogger(SleeTransactionManagerImpl.class);
	
	/**
	 * 
	 */
	private final Transaction tx;
	
	/**
	 * 
	 */
	private final TransactionContextImpl txContext;
	
	/**
	 * 
	 * @param tx
	 * @param txContext
	 */
	public SleeTransactionSynchronization(Transaction tx,TransactionContextImpl txContext) {
		this.tx = tx;
		this.txContext = txContext;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Synchronization#afterCompletion(int)
	 */
	public void afterCompletion(int status) {
		TransactionContextThreadLocal.setTransactionContext(null);
		switch (status) {

		case Status.STATUS_COMMITTED:
			if (logger.isDebugEnabled()) {
				logger.debug("Completed commit of tx " + tx);
			}
			txContext.executeAfterCommitPriorityActions();
			txContext.executeAfterCommitActions();
			break;

		case Status.STATUS_ROLLEDBACK:
			if (logger.isDebugEnabled()) {
				logger.debug("Completed rollback of tx " + tx);
			}
			txContext.executeAfterRollbackActions();
			break;

		default:				
			throw new IllegalStateException("Unexpected transaction state "
					+ status);
		}
		txContext.cleanup();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Synchronization#beforeCompletion()
	 */
	public void beforeCompletion() {			
		txContext.executeBeforeCommitPriorityActions();
		txContext.executeBeforeCommitActions();
	}

}
