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

package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

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
	 * the container's ac factory
	 */
	final ActivityContextFactory acFactory;
	/**
	 * 
	 * @param sleeContainer
	 * @param sleeEndpoint
	 */
	SleeEndpointOperationNotTransactedExecutor(SleeContainer sleeContainer,SleeEndpointImpl sleeEndpoint) {
		this.txManager = sleeContainer.getTransactionManager();
		this.acFactory = sleeContainer.getActivityContextFactory();
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
	 * Suspends the current tx (if exists). If the tx exists will also put a
	 * barrier for the specified handle before suspending.
	 * 
	 * @return the tx which was suspended, null if there is no active tx
	 * @throws SLEEException
	 *             if there is a system error in tx manager
	 */
	Transaction suspendTransactionAndActivity(ActivityHandle handle) throws SLEEException {
		try {
			final Transaction tx = txManager.getTransaction();
			if (tx != null) {
				final ActivityContextHandle ach = new ResourceAdaptorActivityContextHandleImpl(sleeEndpoint.getRaEntity(), handle);
				final LocalActivityContext era = acFactory.getLocalActivityContext(ach, false);
				if (era != null) {
					era.getEventQueueManager().createBarrier(tx);
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							era.getEventQueueManager().removeBarrier(tx);					
						}
					};
					final TransactionContext tc = txManager.getTransactionContext();
					tc.getAfterCommitActions().add(action);
					tc.getAfterRollbackActions().add(action);
				}
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
