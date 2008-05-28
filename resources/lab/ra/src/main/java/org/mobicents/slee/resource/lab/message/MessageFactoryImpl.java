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


/**
 * MessageFactoryImpl implements the functions of MessageFactory.
 *
 * @author Michael Maretzke
 */
public class MessageFactoryImpl implements MessageFactory {
    
    public MessageFactoryImpl() {
    }
    
    public Message createMessage(String id, String command) {
        return MessageImpl.getInstance(id, command);
    }

    public MessageEvent createMessageEvent(Object obj, Message message) {
        return MessageEventImpl.getInstance(obj, message);
    }
}
