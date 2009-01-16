package javax.slee.transaction;

import javax.transaction.RollbackException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.SystemException;

/**
 * The <code>CommitListener</code> interface defines the callback operations that
 * enable a Resource Adaptor to be notified of the outcome of an asynchronous
 * commit operation.  An asynchronous commit operation is performed by invoking
 * either {@link SleeTransaction#asyncCommit} or {@link SleeTransactionManager#asyncCommit}.
 */
public interface CommitListener {
    /**
     * This method is invoked by the SLEE to indicate the transaction commit was successful.
     */
    public void committed();

    /**
     * This method is invoked by the SLEE to indicate the transaction rolled back
     * instead of committing.
     * @param rbe the <code>RollbackException</code> which may provide information
     *        about why the transaction rolled back.
     */
    public void rolledBack(RollbackException rbe);

    /**
     * This method is invoked by the SLEE to report that a heuristic decision was made
     * and that some relevant updates have been committed while others have been rolled back.
     * @param hme the <code>HeuristicMixedException</code> which may provide information
     *        about the heuristic decision.
     */
    public void heuristicMixed(HeuristicMixedException hme);

    /**
     * This method is invoked by the SLEE to indicate that a heuristic decision was made
     * and that all relevant updates have been rolled back.
     * @param hrbe the <code>HeuristicRollbackException</code> which may provide information
     *        about the heuristic decision.
     */
    public void heuristicRollback(HeuristicRollbackException hrbe);

    /**
     * This method is invoked by the SLEE to indicate that the transaction manager
     * encountered an unexpected error while attempting to commit the transaction.
     * @param se the <code>SystemException</code> which may provide information
     *        about the unexpected error.
     */
    public void systemException(SystemException se);
}
