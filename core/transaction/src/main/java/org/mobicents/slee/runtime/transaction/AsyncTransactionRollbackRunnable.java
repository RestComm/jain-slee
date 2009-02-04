package org.mobicents.slee.runtime.transaction;

import javax.slee.transaction.RollbackListener;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * {@link Runnable} that executes a {@link Transaction} async rollback.
 * @author martins
 *
 */
public class AsyncTransactionRollbackRunnable implements Runnable {

	private final RollbackListener rollbackListener;
	private final Transaction transaction;
		
	public AsyncTransactionRollbackRunnable(RollbackListener rollbackListener,
			Transaction transaction) {		
		this.rollbackListener = rollbackListener;
		this.transaction = transaction;
	}

	public void run() {
		try {
			transaction.rollback();
			if (rollbackListener != null) {
				rollbackListener.rolledBack();
			}
		} catch (SystemException e) {
			if (rollbackListener != null) {
				rollbackListener.systemException(e);
			}
		} catch (Exception e) {
			if (rollbackListener != null) {
				rollbackListener.systemException(new SystemException(e.getMessage()));
			}
		}
	}

}
