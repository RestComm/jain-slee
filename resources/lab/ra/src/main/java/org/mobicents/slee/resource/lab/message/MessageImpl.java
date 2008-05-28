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
 * The MessageImpl object implements the Message interface and is the concrete 
 * implementation class of the more abstract interface which is the only visible 
 * interface for the SBB.
 *
 * @author Michael Maretzke
 */
public class MessageImpl implements Message {
    private String id;
    private String command;
    private int commandId;
    
    private int state;
    
    /**
     * The factory method to generate a new instance of Message.
     * 
     * @param id the id of the underlying protocol message
     * @param command the command of the underlying protocol message
     * @return a newly created Message object
     */
    public static Message getInstance(String id, String command) {
        return new MessageImpl(id, command);
    }
    
    private MessageImpl(String id, String command) {
        this.id = id;
        this.command = command;
        if (command.compareToIgnoreCase("INIT") == 0) commandId = Message.INIT;
        else if (command.compareToIgnoreCase("ANY") == 0) commandId = Message.ANY;
        else commandId = Message.END;
    }
    
    public String getId() {
        return id;
    }
    
    public String getCommand() {
        return command;
    }    
    
    public int getCommandId() {
        return commandId;
    }
}
