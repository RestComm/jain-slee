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
 * The Message represents the means of communication in the RAFStack resource 
 * adaptor defined protocol. It abstracts the underlying protocol information 
 * by wrapping the protocol information into Java objects.<br>
 * One protocol message of the defined protocl contains two elements:<br>
 * An identifier (id)<br>
 * A command string (command)<br>
 * Message follows the value object pattern and can only be constructed by 
 * a factory object.
 *
 * @author Michael Maretzke
 */
public interface Message {
    // possible commands of the protocol
    public final static int INIT = 1;
    public final static int ANY = 2;
    public final static int END = 3;
    
    /**
     * Access the message's unique identifier.
     * 
     * @return the message's identifier
     */
    public String getId();
    
    /**
     * Access the message's command string.
     *
     * @return the command string
     */
    public String getCommand();
    
    /**
     * Access the command string's identifier. The integer representation 
     * of the command string. May be either Message.INIT, Message.ANY or 
     * Message.END.
     */
    public int getCommandId(); 
}
