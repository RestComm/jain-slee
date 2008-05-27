/*
 * ResponseEventImpl.java
 *
 * Created on 19 Декабрь 2006 г., 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.ResponseEvent;
import net.java.slee.resource.smpp.ShortMessage;

/**
 *
 * @author Oleg Kulikov
 */
public class ResponseEventImpl implements ResponseEvent {
    
    private ClientTransaction tx;
    private ShortMessage message;
    
    /** Creates a new instance of ResponseEventImpl */
    public ResponseEventImpl(ClientTransaction tx, ShortMessage message) {
        this.tx = tx;
        this.message = message;
    }
    
    public ClientTransaction getTransaction() {
        return tx;
    }
    
    public ShortMessage getMessage() {
        return message;
    }
}
