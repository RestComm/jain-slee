/*
 * TransactionIDAccessImpl.java
 * 
 * Created on Dec 30, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.resource;

import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.sbbapi.TransactionIDAccess;

/**
 *
 */
public class TransactionIDAccessImpl implements TransactionIDAccess {

    
    
    private TransactionManagerImpl transactionManager;
    
    public TransactionIDAccessImpl(TransactionManagerImpl transactionManager ) {
        this.transactionManager = transactionManager;
    }
    /* (non-Javadoc)
     * @see com.opencloud.sleetck.lib.resource.sbbapi.TransactionIDAccess#getCurrentTransactionID()
     */
    public Object getCurrentTransactionID() throws TCKTestErrorException {
        
        // Note that we cannot return the UserTransaction because it is not serializable.
        
        //return new Integer( transactionManager.getCurrentTransaction().hashCode());
        
        return new Integer(transactionManager.getTransaction().hashCode());
    }

}

