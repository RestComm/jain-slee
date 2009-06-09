package org.mobicents.slee.runtime.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.transaction.CommitListener;
import javax.slee.transaction.RollbackListener;
import javax.slee.transaction.SleeTransaction;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

public class SleeTransactionImpl implements SleeTransaction {

	/**
	 * thread pool for async commit/rollback operations
	 */
	private static final ExecutorService executorService = Executors
			.newCachedThreadPool();

	/**
	 * the wrapped JBossTS transaction
	 */
	private final TransactionImple transaction;

	/**
	 * caching the wrapped transaction id
	 */
	private final String transactionId;

	/**
	 * the transaction manager
	 */
	private final SleeTransactionManagerImpl transactionManager;

	private boolean asyncOperationInitiated = false;
	
	public SleeTransactionImpl(TransactionImple transaction,
			SleeTransactionManagerImpl transactionManager) {
		this.transaction = transaction;
		this.transactionId = transaction.get_uid().toString();
		this.transactionManager = transactionManager;
	}

	/**
	 * Some operations require that the transaction be suspended
	 * @throws SystemException
	 */
	private void suspendIfAssoaciatedWithThread() throws SystemException {
		// if there is a tx associated with this thread and it is this one
		// then suspend it to dissociate the thread
		SleeTransaction currentThreadTransaction = transactionManager
				.getSleeTransaction();
		if (currentThreadTransaction != null
				&& currentThreadTransaction.equals(this)) {
			transactionManager.suspend();
		}		
	}
	
	/**
	 * Verifies if the wrapped transaction is active and if dissociates it from
	 * the thread if needed
	 * 
	 * @throws IllegalStateException
	 * @throws SecurityException
	 */
	private void beforeAsyncOperation() throws IllegalStateException,
			SecurityException {
		try {
			int status = transaction.getStatus();
			if (status != Status.STATUS_ACTIVE
					&& status != Status.STATUS_MARKED_ROLLBACK) {
				throw new IllegalStateException(
						"There is no active tx, tx is in state: " + status);
			}
			asyncOperationInitiated = true;
			suspendIfAssoaciatedWithThread();
		} catch (SystemException e) {
			throw new IllegalStateException(e);
		}
	}

	public void asyncCommit(CommitListener commitListener)
			throws IllegalStateException, SecurityException {
		beforeAsyncOperation();
		executorService.submit(new AsyncTransactionCommitRunnable(
				commitListener, transaction));
	}

	public void asyncRollback(RollbackListener rollbackListener)
			throws IllegalStateException, SecurityException {
		beforeAsyncOperation();
		executorService.submit(new AsyncTransactionRollbackRunnable(
				rollbackListener, transaction));
	}

	public boolean delistResource(XAResource arg0, int arg1)
			throws IllegalStateException, SystemException {
		return transaction.delistResource(arg0, arg1);
	}

	public boolean enlistResource(XAResource arg0)
			throws IllegalStateException, RollbackException {
		try {
			return transaction.enlistResource(arg0);
		} catch (SystemException e) {
			// this should be a bug in slee 1.1 api, the exceptions thrown
			// should match jta transaction interface
			throw new RuntimeException(e);
		}
	}

	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {	
		
		if (asyncOperationInitiated) {
			throw new IllegalStateException();
		}
		
		try {
			transaction.commit();		
		}
		finally {
			suspendIfAssoaciatedWithThread();
		}
	}

	public int getStatus() throws SystemException {
		return transaction.getStatus();
	}

	public void registerSynchronization(Synchronization sync)
			throws RollbackException, IllegalStateException, SystemException {
		transaction.registerSynchronization(sync);
	}

	public void rollback() throws IllegalStateException, SystemException {
		
		if (asyncOperationInitiated) {
			throw new IllegalStateException();
		}
		
		try {
			transaction.rollback();		
		}
		finally {
			suspendIfAssoaciatedWithThread();
		}		
	}

	public void setRollbackOnly() throws IllegalStateException, SystemException {
		transaction.setRollbackOnly();
	}

	@Override
	public String toString() {
		return transactionId;
	}

	@Override
	public int hashCode() {
		return transactionId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((SleeTransactionImpl) obj).transactionId
					.equals(this.transactionId);
		} else {
			return false;
		}
	}

}
