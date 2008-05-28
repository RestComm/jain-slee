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

import org.mobicents.slee.resource.lab.message.Message;
import org.mobicents.slee.resource.lab.ratype.MessageActivity;


/**
 * The RAFActivityImpl implements the RAFActivity interface. For further information,
 * please refer to the description of the interface RAFActivity.
 *
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageActivityImpl implements MessageActivity {
    // possible states of the resource adaptor according to the 
    // protocol definition
    private final static int NOTEXISTING = 0;
    private final static int ACTIVE = 1;
    private final static int ENDED = 2;
    private int state;
    // timestamp of creation of activity
    private long initTime;
    // counter for the various commands
    private int initCounter;
    private int anyCounter;
    private int endCounter;
    
    private String sessionId;
    
    public MessageActivityImpl(String sessionId) {
        initTime = System.currentTimeMillis();
        state = NOTEXISTING;
        this.sessionId = sessionId;
    }
    
    public void initReceived() { 
        if (isValid(Message.INIT)) {
            initCounter++;
            state = ACTIVE;
        }         
    }
    public void anyReceived() { 
        if (isValid(Message.ANY)) {
            anyCounter++;
            state = ACTIVE;
        }         
    }
    public void endReceived() { 
        if (isValid(Message.END)) {
            endCounter++; 
            state = ENDED;            
        }
    }
    
    public int getInitCounter() { return initCounter; }
    public int getAnyCounter() { return anyCounter; }
    public int getEndCounter() { return endCounter; }
    
    public long getStartTime() { return initTime; }
    
    /**
     * Checks if an incoming command is valid according to the 
     * defined statemachine. The statemachine of the resource adaptor
     * is described above.
     *
     * @param command the integer representation of the command
     */
    public boolean isValid(int command) {
        if ((state == NOTEXISTING) && (command == Message.INIT)) 
            return true;
        if ((state == ACTIVE) && ((command == Message.ANY) || (command == Message.END)))
            return true;
        return false;
    }
    
    public String getSessionId(){
    	return this.sessionId;
    }
}
