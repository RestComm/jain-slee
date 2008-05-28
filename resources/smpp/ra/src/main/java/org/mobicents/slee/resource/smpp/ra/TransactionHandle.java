/*
 * ServerTransactionHandle.java
 *
 * Created on 19 Декабрь 2006 г., 10:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import javax.slee.resource.ActivityHandle;

import net.java.slee.resource.smpp.Transaction;

/**
 *
 * @author Oleg Kulikov
 */
public class TransactionHandle implements ActivityHandle {
    
    private Transaction tx;
    
    /** Creates a new instance of ServerTransactionHandle */
    public TransactionHandle(Transaction tx) {
        this.tx = tx;
    }
       
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((TransactionHandle)o).tx.getId() == this.tx.getId();
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return tx.getId();
    }       
    
}
