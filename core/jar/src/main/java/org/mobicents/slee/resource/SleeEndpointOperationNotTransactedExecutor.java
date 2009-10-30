package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Base for executors of slee endpoint operations, which must be executed out of
 * any tx context.
 * 
 * @author martins
 * 
 */
public abstract class SleeEndpointOperationNotTransactedExecutor {

	/**
	 * the container's tx manager
	 */
	final SleeTransactionManager txManager;
	
	/**
	 * the endpoint bound to the executor
	 */
	final SleeEndpointImpl sleeEndpoint;
	
	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	SleeEndpointOperationNotTransactedExecutor(SleeContainer sleeContainer,SleeEndpointImpl sleeEndpoint) {
		this.txManager = sleeContainer.getTransactionManager();
		this.sleeEndpoint = sleeEndpoint;
	}
	
	/**
	 * Suspends the current tx (if exists).
	 * @return the tx which was suspended, null if there is no active tx
	 * @throws SLEEException if there is a system error in tx manager
	 */
	Transaction suspendTransaction() throws SLEEException {
		try {
			final Transaction tx = txManager.getTransaction();
			if (tx != null) {
				txManager.suspend();
			}
			return tx;
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}
	
	/**
	 * Resumes the specified tx. If it is null nothing is done.
	 * @param transaction
	 * @throws SLEEException if there is a system error in tx manager
	 */
	void resumeTransaction(Transaction transaction) throws SLEEException {
		if (transaction != null) {
			try {
				txManager.resume(transaction);
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
	}
}
