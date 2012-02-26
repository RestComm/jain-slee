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

/**
 * The MessageEvent wraps an Message object and adds the footprint of the requesting
 * object to it.  
 *
 * @author Michael Maretzke
 */
public interface MessageEvent {
    /**
     * Access the wrapped Message object which is contained in the implementation
     * object.
     *
     * @return the contained Message object.
     */
    public Message getMessage();
    
    /**
     * The object on which the Event initially occurred.
     *
     * @return   The object on which the Event initially occurred.
     */
    public Object getSource();
}

