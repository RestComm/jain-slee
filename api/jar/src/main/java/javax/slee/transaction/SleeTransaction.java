package javax.slee.transaction;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

/**
 * The <code>SleeTransaction</code> interface extends the JTA <code>Transaction</code>
 * interface by providing asynchronous commit and rollback operations.
 * @since SLEE 1.1
 */
public interface SleeTransaction extends Transaction {
    /**
     * Request that the transaction represented by this <code>SleeTransaction</code> object
     * asynchronously commit.  The calling thread is not required to have this transaction
     * associated with the thread.  The transaction must be currently active
     * ({@link javax.transaction.Status#STATUS_ACTIVE Status.STATUS_ACTIVE}) or marked for rollback
     * ({@link javax.transaction.Status#STATUS_MARKED_ROLLBACK Status.STATUS_MARKED_ROLLBACK}).
     * This method initiates the commit operation and may return before the transaction
     * completes.  If any threads are associated with the transaction then those associations
     * are cleared before this method returns.
     * <p>
     * If the transaction has been marked for rollback, or if execution of any
     * {@link javax.transaction.Synchronization#beforeCompletion} callbacks cause it to
     * be marked for rollback, then a rollback will be started (equivalent to calling
     * {@link #asyncRollback}).
     * <p>
     * At some point after calling this method, the transaction state will become either
     * {@link javax.transaction.Status#STATUS_COMMITTED Status.STATUS_COMMITTED} or
     * {@link javax.transaction.Status#STATUS_ROLLEDBACK Status.STATUS_ROLLEDBACK}.
     * @param listener listener object that will receive callbacks depending on the final
     *        outcome of the transaction.  If this argument is <code>null</code>, no callbacks
     *        will be made by the SLEE.
     * @throws IllegalStateException if the transaction is not in the active or marked for
     *        rollback state.
     * @throws SecurityException if the current thread is not allowed to commit the transaction.
     */
    public void asyncCommit(CommitListener listener)
        throws IllegalStateException, SecurityException;

    /**
     * Request that the transaction represented by this <code>SleeTransaction</code> object
     * asynchronously roll back.  The calling thread is not required to have this transaction
     * associated with the thread.  The transaction must be currently active
     * ({@link javax.transaction.Status#STATUS_ACTIVE Status.STATUS_ACTIVE}) or marked for rollback
     * ({@link javax.transaction.Status#STATUS_MARKED_ROLLBACK Status.STATUS_MARKED_ROLLBACK}).
     * This method initiates the rollback operation and may return before the transaction
     * completes.  If any thread are associated with the transaction then those associations
     * are cleared before this method returns.
     * <p>
     * At some point after calling this method, the transaction state will become
     * {@link javax.transaction.Status#STATUS_ROLLEDBACK Status.STATUS_ROLLEDBACK}.
     * @param listener listener object that will receive callbacks depending on the final
     *        outcome of the transaction.  If this argument is <code>null</code>, no callbacks
     *        will be made by the SLEE.
     * @throws IllegalStateException if the transactions is not in the active or marked for
     *        rollback state.
     * @throws SecurityException if the current thread is not allowed to roll back the transaction.
     */
    public void asyncRollback(RollbackListener listener)
        throws IllegalStateException, SecurityException;

    /**
     * This method, defined by the JTA <code>Transaction</code> interface, has no defined
     * behavior in SLEE 1.1.
     */
    public boolean enlistResource(XAResource resource)
        throws IllegalStateException, RollbackException;

    /**
     * This method, defined by the JTA <code>Transaction</code> interface, has no defined
     * behavior in SLEE 1.1.
     */
    public boolean delistResource(XAResource resource, int flag)
        throws IllegalStateException, SystemException;

    /**
     * Compare this SLEE Transaction object for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a <code>SleeTransaction</code> object
     *        that represents the same transaction as this, <code>false</code> otherwise.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj);

    /**
     * Get a hash code value for the transaction represented by this SLEE Transaction object.
     * Two <code>SleeTransaction</code> objects <code>t1</code> and <code>t2</code> must
     * return the same hash code if <code>t1.equals(t2)</code>.
     * @return a hash code value for this SLEE Transaction object.
     * @see Object#hashCode()
     */
    public int hashCode();

    /**
     * Get a string representation for the transaction represented by this
     * <code>SleeTransaction</code> object.  For any two <code>SleeTransaction</code>
     * objects <code>t1</code> and <code>t2</code>:
     * <ul>
     *   <li>if <code>t1</code> and <code>t2</code> represent the same transaction,
     *       ie. <code>t1.equals(t2)</code>, then <code>t1.toString().equals(t2.toString())</code>
     *       must hold true.
     *   <li>if <code>t1</code> and <code>t2</code> represent different transactions,
     *       then <code>!t1.toString().equals(t2.toString())</code> must hold true.
     * </ul>
     * @return a string representation for the transaction.
     * @see Object#toString()
     */
    public String toString();
}
