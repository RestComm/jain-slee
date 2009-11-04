/*
 * ServerTransactionHandle.java
 *
 * Created on 19 Декабрь 2006 г., 10:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import net.java.slee.resource.smpp.Transaction;

/**
 *
 * @author Oleg Kulikov
 */
public class TransactionHandle implements SmppActivityHandle {
    
    private int id;
    
    public TransactionHandle(int id) {
        this.id = id;
    }
       
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((TransactionHandle)o).id == this.id;
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return id;
    }       
    
}
