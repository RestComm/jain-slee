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
