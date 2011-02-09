package org.mobicents.slee.container.transaction;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.mobicents.slee.container.SleeContainerModule;

/** 
 * 
 * Transaction manager contract within SLEE. 
 * Provides convenience methods for tx and its context handling.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 * 
 */
public interface SleeTransactionManager extends SleeContainerModule, javax.slee.transaction.SleeTransactionManager {
	
    public static final String JNDI_NAME = "SleeTransactionManager";
    
	/** Verifies if we are in the context of a transaction.
	 * 
	 * @throws  TransactionRequiredLocalException if we are not in the context of a transaction.
	 */
	public void mandateTransaction() throws TransactionRequiredLocalException;
	
	/**
	 * Ensures a transaction exists, i.e., if there is no transaction one is created
	 * @return true if a transaction was created
	 */
	public boolean requireTransaction();
	
	/**
	 * Executes logic to complete the handling of a slee transaction by a method that requires a tx context. to be used as a complement of requireTransaction()
	 *  
	 * @param terminateTx indicates if the logic should terminate the tx or not
	 * @param doRollback if true indicates a rollback should be done, either by terminating the tx with rollback() or by just setting the rollback only flag
	 * @param txManager the tx manager to use
	 * @throws SLEEException if the transaction can't be finalized
	 * @throws SystemException 
	 * @throws SecurityException 
	 * @throws IllegalStateException 
	 * @throws HeuristicRollbackException 
	 * @throws HeuristicMixedException 
	 * @throws RollbackException 
	 */
	public void requireTransactionEnd(boolean terminateTx, boolean doRollback) throws IllegalStateException, SecurityException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException;

	/**
	 * Retrieves the context object associated with the current transaction
	 * @return null if there is no active transaction
	 */
	public TransactionContext getTransactionContext();
	
	/**
	 * 
	 * @return true if the current transaction is marked for rollback
	 * @throws SystemException
	 */
    public boolean getRollbackOnly() throws SystemException;
    
    /**
     * Retrieves the underlying transaction manager.
     * @return
     */
    public TransactionManager getRealTransactionManager();
    
}
