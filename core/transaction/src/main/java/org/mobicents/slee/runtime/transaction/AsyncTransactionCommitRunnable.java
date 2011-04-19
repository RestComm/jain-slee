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

import javax.slee.transaction.CommitListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * {@link Runnable} that executes a {@link Transaction} async commit.
 * @author martins
 *
 */
public class AsyncTransactionCommitRunnable implements Runnable {

	private final CommitListener commitListener;
	private final Transaction transaction;
		
	public AsyncTransactionCommitRunnable(CommitListener commitListener,
			Transaction transaction) {
		this.commitListener = commitListener;
		this.transaction = transaction;
	}

	public void run() {
		try {
			transaction.commit();
			if (commitListener != null) {
				commitListener.committed();
			}
		} catch (RollbackException e) {
			if (commitListener != null) {
				commitListener.rolledBack(e);
			}
		} catch (HeuristicMixedException e) {
			if (commitListener != null) {
				commitListener.heuristicMixed(e);
			}
		} catch (HeuristicRollbackException e) {
			if (commitListener != null) {
				commitListener.heuristicRollback(e);
			}
		} catch (SystemException e) {
			if (commitListener != null) {
				commitListener.systemException(e);
			}
		} catch (Exception e) {
			if (commitListener != null) {
				commitListener.systemException(new SystemException(e.getMessage()));
			}
		}
	}

}
