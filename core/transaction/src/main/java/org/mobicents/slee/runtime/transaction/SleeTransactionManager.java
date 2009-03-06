 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.runtime.transaction;

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

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
public interface SleeTransactionManager extends javax.slee.transaction.SleeTransactionManager {
	
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
	 * retrieves the context object associated with the current transaction
	 * @return
	 * @throws SystemException
	 */
	public TransactionContext getTransactionContext() throws SystemException;
	
	/**
	 * adds a new {@link TransactionalAction} that will be executed as soon as the transaction commits
	 * @param action
	 * @throws SystemException
	 */
	public void addAfterCommitPriorityAction(TransactionalAction action) throws SystemException; 
	
	/**
	 * adds a new {@link TransactionalAction} that will be executed after the priority actions are executed, which is when the transaction commits
	 * @param action
	 * @throws SystemException
	 */
	public void addAfterCommitAction(TransactionalAction action) throws SystemException; 
	
	/**
	 * Convinience method to add a {@link TransactionalAction} to the
	 * {@link TransactionContext} and execute it if rollback occurrs. This
	 * method will ignore the action if there is no valid transaction, instead of failing.
	 * 
	 * @param action
	 * @throws SystemException 
	 */
	public void addAfterRollbackAction(TransactionalAction action) throws SystemException; 
	
	/**
	 * adds a new {@link TransactionalAction} that will be executed before the transaction is committed
	 * @param action
	 * @throws SystemException
	 */
	public void addBeforeCommitAction(TransactionalAction action) throws SystemException; 
	
	/**
	 * 
	 * @return true if the current transaction is marked for rollback
	 * @throws SystemException
	 */
    public boolean getRollbackOnly() throws SystemException;
    
   /**
     * @return String - a list of ongoing SLEE transactions
     */
    public String displayOngoingSleeTransactions();
    
}
