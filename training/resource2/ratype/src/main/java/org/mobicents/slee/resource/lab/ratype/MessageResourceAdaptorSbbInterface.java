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
package org.mobicents.slee.resource.lab.ratype;

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.message.MessageFactory;

/**
 * RAFrameResourceAdaptorSbbInterface describes the interface between SBBs 
 * and the RAFrame resource adaptor. This interface provides means for SBBs 
 * to communicate with the resource adaptor.
 * <br>
 * RAFrameResourceAdaptorSbbInterface is defined in the deployment descriptor 
 * file "resource-adaptor-type-jar.xml". Therein, the tag <resource-adaptor-interface>
 * and its sub-tag <resource-adaptor-interface-name> reference to 
 * com.maretzke.raframe.ratype.RAFrameResourceAdaptorSbbInterface.
 * <br>
 * To be available for SBBs the deployment descriptor file "sbb-jar.xml" needs 
 * to bind a JNDI name to this interface. This is done in the tag
 * <resource-adaptor-entity-binding>. The tag <resource-adaptor-object-name> defines
 * the JNDI name of this interface: "slee/resources/raframe/1.0/sbb2ra".
 * <br>
 * The class com.maretzke.raframe.ra.RAFrameProvider implements this interface.
 * For further information, please refer to JAIN SLEE Specification 1.0, Final 
 * Release Page 239 and following pages.
 *
 * @author Michael Maretzke
 * @author amit bhayani
 */
public interface MessageResourceAdaptorSbbInterface {
    /**
     * send() is a functionality of the resource adaptor exposed to the Sbb. 
     * Due to the architecture of the Sbb having only access to the RAFrameProvider
     * instead of the RAFrameResourceAdaptor directly, the resource adaptor developer
     * is able to limit the privileges of a Sbb to invoke methods of the resource adaptor
     * directly. 
     *
     * @param message is the message to send
     */
    public void send(Message message);
    
    /**
     * The MessageFactory is needed inside the SBB to create Message objects, which are 
     * sent through the resource adaptor.
     */
    public MessageFactory getMessageFactory();
    
}
