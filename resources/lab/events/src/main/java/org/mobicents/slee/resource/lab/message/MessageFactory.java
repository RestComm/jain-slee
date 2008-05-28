/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.message;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageEvent;

/**
 * The MessageFactory interface defines the methods supported by the Factory.
 * Currently, the MessageFactory is able to create:<br>
 * Message objects<br>
 * MessageEvent objects<br> 
 *
 * @author Michael Maretzke
 */
public interface MessageFactory {    
    public Message createMessage(String id, String command);
    public MessageEvent createMessageEvent(Object obj, Message message);
}
