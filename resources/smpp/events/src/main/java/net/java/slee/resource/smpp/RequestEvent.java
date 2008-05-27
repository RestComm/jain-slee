/*
 * RequestEvent.java
 *
 * Created on 6 Декабрь 2006 г., 10:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.slee.resource.smpp;

/**
 *
 * @author Oleg Kulikov
 */
public interface RequestEvent extends SmppEvent {
    public ServerTransaction getTransaction();
}
