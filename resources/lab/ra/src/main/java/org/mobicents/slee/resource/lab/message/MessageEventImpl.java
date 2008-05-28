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

import java.util.EventObject;

/**
 * The MessageEvent wraps an Message object and adds the footprint of the requesting
 * object to it.
 * The implementation class MessageEventImpl implements the interface MessageEvent.
 *
 * @author Michael Maretzke
 */
public class MessageEventImpl extends EventObject implements MessageEvent {
    
    private Message message;
    
    /**
     * The factory method to generate a new instance of MessageEvent.
     * 
     * @param obj the generating object's reference
     * @param message message to attach to this event object
     * @return a newly created MessageEvent object
     */
    public static MessageEvent getInstance(Object obj, Message message) {
        return new MessageEventImpl(obj, message);
    }
    
    /**
     * Creates a new instance of MessageEvent 
     */
    private MessageEventImpl(Object source, Message message) {
        super(source);
        this.message = message;
    }
    
    public Message getMessage() {
        return message;
    }    
}
