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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.slee.transaction.CommitListener;
import javax.slee.transaction.RollbackListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;

import org.mobicents.slee.container.transaction.SleeTransaction;
import org.mobicents.slee.container.transaction.TransactionContext;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

/**
 * Implementation of the {@link SleeTransaction}.
 * @author martins
 *
 */
public class SleeTransactionImpl implements SleeTransaction {

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

	/**
	 * the tx context, stored here too to support suspend/resume op 
	 */
	private final TransactionContext txContext; 
	
	/**
	 * controls thread access safety
	 */
	private AtomicBoolean asyncOperationInitiated = new AtomicBoolean(false);
	
	/**
	 * 
	 * @param transaction
	 * @param txContext
	 * @param transactionManager
	 */
	public SleeTransactionImpl(TransactionImple transaction,
			TransactionContext txContext, SleeTransactionManagerImpl transactionManager) {
		this.transaction = transaction;
		this.transactionId = transaction.get_uid().toString();
		this.txContext = txContext;
		this.transactionManager = transactionManager;
	}

	/**
	 * Retrieves the tx context.
	 * @return
	 */
	public TransactionContext getTransactionContext() {
		return txContext;
	}
	
	/**
	 * Retrieves the real jta transaction
	 * @return
	 */
	TransactionImple getWrappedTransaction() {
		return transaction;
	}
	
	/**
	 * Some operations require that the transaction be suspended
	 * @throws SystemException
	 */
	private void suspendIfAssoaciatedWithThread() throws SystemException {
		// if there is a tx associated with this thread and it is this one
		// then suspend it to dissociate the thread (dumb feature?!?! of jboss ts)
		final SleeTransaction currentThreadTransaction = transactionManager
				.getSleeTransaction();
		if (currentThreadTransaction != null
				&& currentThreadTransaction.equals(this)) {
			// lets use the real tx manager directly, to avoid any other procedures
			transactionManager.getRealTransactionManager().suspend();
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
			if (asyncOperationInitiated.getAndSet(true) || (status != Status.STATUS_ACTIVE
					&& status != Status.STATUS_MARKED_ROLLBACK)) {
				throw new IllegalStateException(
						"There is no active tx, tx is in state: " + status);
			}
			suspendIfAssoaciatedWithThread();
		} catch (SystemException e) {
			throw new IllegalStateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransaction#asyncCommit(javax.slee.transaction.CommitListener)
	 */
	public void asyncCommit(CommitListener commitListener)
			throws IllegalStateException, SecurityException {
		beforeAsyncOperation();
		transactionManager.getExecutorService().submit(new AsyncTransactionCommitRunnable(
				commitListener, transaction));
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransaction#asyncRollback(javax.slee.transaction.RollbackListener)
	 */
	public void asyncRollback(RollbackListener rollbackListener)
			throws IllegalStateException, SecurityException {
		beforeAsyncOperation();
		transactionManager.getExecutorService().submit(new AsyncTransactionRollbackRunnable(
				rollbackListener, transaction));
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransaction#delistResource(javax.transaction.xa.XAResource, int)
	 */
	public boolean delistResource(XAResource arg0, int arg1)
			throws IllegalStateException, SystemException {
		return transaction.delistResource(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.transaction.SleeTransaction#enlistResource(javax.transaction.xa.XAResource)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Transaction#commit()
	 */
	public void commit() throws RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SecurityException,
			IllegalStateException, SystemException {	
		if (asyncOperationInitiated.get()) {
			throw new IllegalStateException();
		}
		try {
			transaction.commit();		
		}
		finally {
			suspendIfAssoaciatedWithThread();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Transaction#getStatus()
	 */
	public int getStatus() throws SystemException {
		return transaction.getStatus();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Transaction#registerSynchronization(javax.transaction.Synchronization)
	 */
	public void registerSynchronization(Synchronization sync)
			throws RollbackException, IllegalStateException, SystemException {
		transaction.registerSynchronization(sync);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Transaction#rollback()
	 */
	public void rollback() throws IllegalStateException, SystemException {
		if (asyncOperationInitiated.get()) {
			throw new IllegalStateException();
		}
		try {
			transaction.rollback();		
		}
		finally {
			suspendIfAssoaciatedWithThread();
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.transaction.Transaction#setRollbackOnly()
	 */
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
