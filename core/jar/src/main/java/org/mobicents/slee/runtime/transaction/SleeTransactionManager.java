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

import java.util.List;

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.mobicents.slee.runtime.cache.OldCacheManager;

/** 
 * 
 * Transaction manager contract within SLEE. 
 * Provides convenience methods for tx handling 
 * as well as access to tx cache.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * 
 */
public interface SleeTransactionManager extends OldCacheManager, TransactionManager {
	
    public static final String JNDI_NAME = "SleeTransactionManager";
    
	/** Check if we are in the context of a transaction.
	 * 
	 *@throws  TransactionRequiredLocalException if we are not in the context of a transaction.
	 *
	 */
	public void mandateTransaction() throws TransactionRequiredLocalException;
	
	public boolean requireTransaction();
	
	public boolean isInTx() throws SystemException;
	
	public void begin() throws SystemException;
	
	public void commit() throws SystemException;
	
	public void rollback() throws SystemException;
	
	public void addAfterCommitAction(TransactionalAction action);
	
	public void addAfterRollbackAction(TransactionalAction action);
	
	
	public void addPrepareCommitAction(TransactionalAction action );
	
	public void setRollbackOnly() throws SystemException;
    
    public boolean getRollbackOnly() throws SystemException;
    
    public List getPrepareActions() throws SystemException;
    
    public Transaction getTransaction() throws SystemException;
	
	//for debugging only
    public void assertIsInTx();
    
    public void assertIsNotInTx();

    /**
     * @return String - a list of ongoing SLEE transactions
     */
    public String displayOngoingSleeTransactions();

    
    
}
