package javax.slee.transaction;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * The <code>SleeTransactionManager</code> interface extends the JTA
 * <code>TransactionManager</code> interface by providing asynchronous commit and rollback
 * operations.
 * @since SLEE 1.1
 */
public interface SleeTransactionManager extends TransactionManager {
    /**
     * Begin a new transaction and associate it with the current thread.  This method is
     * equivalent to:<br>
     * <ul><code>
     *   SleeTransactionManager txManager = ...;<br>
     *   txManager.begin();<br>
     *   SleeTransaction tx = txManager.getSleeTransaction();
     * </code></ul>
     * <p>
     * This method is a non-transactional method, however after this method returns a new
     * transaction has become associated with the calling thread.
     * @return a <code>SleeTransaction</code> object representing the new transaction.
     * @throws NotSupportedException if the thread is already associated with a transaction
     *        and the transaction manager implementation does not support nested transactions.
     * @throws SystemException if the transaction could not be started due to a system-level
     *        failure.
     */
    public SleeTransaction beginSleeTransaction()
        throws NotSupportedException, SystemException;

    /**
     * Get the <code>SleeTransaction</code> object that represents the transaction context of the
     * calling thread.  This method is a convenience method equivalent to:
     * <ul><code>
     *   SleeTransactionManager txManager = ...;<br>
     *   SleeTransaction tx = txManager.asSleeTransaction(txManager.getTransaction());
     * </code></ul>
     * <p>
     * This method is a non-transactional method.
     * @return the <code>SleeTransaction</code> object that represents the transaction context
     *        of the calling thread, or <code>null</code> if the thread is not currently
     *        associated with a transaction.
     * @throws SystemException if the transaction object could not be obtained due to a
     *        system-level failure.
     */
    public SleeTransaction getSleeTransaction()
        throws SystemException;

    /**
     * Get a <code>SleeTransaction</code> object for a generic <code>Transaction</code>
     * object.
     * @param tx the <code>Transaction</code> object.  The transaction represented by this
     *        object must have been started by this transaction manager.
     * @return a <code>SleeTransaction</code> object for the specified transaction.
     * @throws NullPointerException if <code>tx</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>tx</code> represents a transaction that was
     *        not started by this transaction manager.
     * @throws SystemException if the transaction object could not be obtained due to a
     *        system-level failure.
     */
    public SleeTransaction asSleeTransaction(Transaction tx)
        throws NullPointerException, IllegalArgumentException, SystemException;

    /**
     * Request that the transaction associated with the current thread asynchronously commit.
     * This method initiates the commit operation and may return before the transaction
     * completes, however the association between the transaction and the thread is cleared
     * before returning.
     * <p>
     * If the transaction has been marked for rollback, or if execution of any
     * {@link javax.transaction.Synchronization#beforeCompletion} callbacks cause it to
     * be marked for rollback, then a rollback will be started (equivalent to calling
     * {@link #asyncRollback}).
     * <p>
     * At some point after calling this method, the transaction state will become either
     * {@link javax.transaction.Status#STATUS_COMMITTED Status.STATUS_COMMITTED} or
     * {@link javax.transaction.Status#STATUS_ROLLEDBACK Status.STATUS_ROLLEDBACK}.
     * <p>
     * This method is a mandatory transactional method.
     * @param listener listener object that will receive callbacks depending on the final
     *        outcome of the transaction.  If this argument is <code>null</code>, no callbacks
     *        will be made by the SLEE.
     * @throws IllegalStateException if a transaction is not currently associated with the
     *        calling thread.
     * @throws SecurityException if the current thread is not allowed to commit the transaction.
     */
    public void asyncCommit(CommitListener listener)
        throws IllegalStateException, SecurityException;

    /**
     * Request that the transaction associated with the current thread asynchronously roll back.
     * This method initiates the rollback operation and may return before the transaction
     * completes, however the association between the transaction and the thread is
     * cleared before returning.
     * <p>
     * At some point after calling this method, the transaction state will become
     * {@link javax.transaction.Status#STATUS_ROLLEDBACK Status.STATUS_ROLLEDBACK}.
     * <p>
     * This method is a mandatory transactional method.
     * @param listener listener object that will receive callbacks depending on the final
     *        outcome of the transaction.  If this argument is <code>null</code>, no callbacks
     *        will be made by the SLEE.
     * @throws IllegalStateException if a transaction is not currently associated with the
     *        calling thread.
     * @throws SecurityException if the current thread is not allowed to roll back the transaction.
     */
    public void asyncRollback(RollbackListener listener)
        throws IllegalStateException, SecurityException;
}
