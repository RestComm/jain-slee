/*
 * RequestEventImpl.java
 *
 * Created on 12 Декабрь 2006 г., 19:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import net.java.slee.resource.smpp.RequestEvent;
import net.java.slee.resource.smpp.ShortMessage;
import net.java.slee.resource.smpp.ServerTransaction;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestEventImpl implements RequestEvent {
    
    private ServerTransaction tx;
    private ShortMessage message;
    
    /** Creates a new instance of RequestEventImpl */
    public RequestEventImpl(ServerTransaction tx, ShortMessage message) {
        this.tx = tx;
        this.message = message;
    }

    public ServerTransaction getTransaction() {
        return tx;
    }

    public ShortMessage getMessage() {
        return message;
    }
    
}
