/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
    
    @Override
    public SleeTransaction getTransaction() throws SystemException;
    
}
