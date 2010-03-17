package javax.slee.transaction;

import javax.transaction.SystemException;

/**
 * The <code>RollbackListener</code> interface defines the callback operations that
 * enable a Resource Adaptor to be notified of the outcome of an asynchronous
 * rollback operation.  An asynchronous rollback operation is performed by invoking
 * either {@link SleeTransaction#asyncRollback} or {@link SleeTransactionManager#asyncRollback}.
 */
public interface RollbackListener {
    /**
     * This method is invoked by the SLEE to indicate the transaction rollback was successful.
     */
    public void rolledBack();

    /**
     * This method is invoked by the SLEE to indicate that the transaction manager
     * encountered an unexpected error while attempting to roll back the transaction.
     * @param se the <code>SystemException</code> which may provide information
     *        about the unexpected error.
     */
    public void systemException(SystemException se);
}
