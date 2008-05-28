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
package org.mobicents.slee.resource.lab.ra;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageFactory;
import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;


/**
 * RAFrameProviderImpl implements the interface RAFrameProvider
 * and provides functionalities of the underlying resource adaptor to the Sbb. 
 * <br> 
 * For further information, please refer to the interface RAFrameProvider.
 * 
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageResourceAdaptorSbbInterfaceImpl implements MessageResourceAdaptorSbbInterface { 
    private static Logger logger = Logger.getLogger(MessageResourceAdaptorSbbInterfaceImpl.class);
    private MessageFactory messageFactory;
    // reference to the RAFrame resource adaptor
    private MessageResourceAdaptor ra;
    
    /**
     * Create an instance of RAFrameProvicer
     * 
     * @param ra the parent resource adaptor
     * @param messageFactory the associated message factory
     * @return a new created instance of RAFrameProvider
     */
    public MessageResourceAdaptorSbbInterfaceImpl(MessageResourceAdaptor ra, MessageFactory messageFactory) {
        this.ra = ra;
        this.messageFactory = messageFactory;
    }

    public MessageResourceAdaptor getRAFrameRA() {
        logger.debug("getRAFrameRA() called.");
        return ra;
    }
    
    /**
     * send() is a functionality of the resource adaptor exposed to the Sbb. 
     * Due to the architecture of the Sbb having only access to the RAFrameProvider
     * instead of the RAFrameResourceAdaptor directly, the resource adaptor developer
     * is able to limit the privileges of a Sbb to invoke methods of the resource adaptor
     * directly. 
     *
     * @param message is the message to send
     */
    public void send(Message message) {
        logger.debug("Sending the message to the stack");
        ra.send(message.getId() + ": " + message.getCommand());
    }
    
    // implements RAFrameResourceAdaptorSbbInterface
    public MessageResourceAdaptorSbbInterface getRAFrameProvider() {
        return this;
    }
    
    // implements RAFrameResourceAdaptorSbbInterface
    public MessageFactory getMessageFactory() {
        logger.debug("getMessageFactory() called.");
        return messageFactory;
    }       
}
