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
