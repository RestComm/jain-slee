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

import static org.mobicents.slee.resource.lab.ra.ActivityState.*;

/**
 * The RAFActivityImpl implements the RAFActivity interface. For further information,
 * please refer to the description of the interface RAFActivity.
 *
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageActivityImpl implements MessageActivity {
	
    
    private ActivityState state;
    // timestamp of creation of activity
    private long initTime;
    // counter for the various commands
    private int initCounter;
    private int anyCounter;
    private int endCounter;
    
    private String sessionId;

    private String raImprint;
    public MessageActivityImpl(String sessionId, String raImprint) {
        initTime = System.currentTimeMillis();
        state = NOTEXISTING;
        this.sessionId = sessionId;
        this.raImprint = raImprint;
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

	public String getRaImprint() {
		return raImprint;
	}

	public void setRaImprint(String raImprint) {
		this.raImprint = raImprint;
	}

	@Override
	public String toString() {
		return "MessageActivityImpl [state=" + state + ", initTime=" + initTime
				+ ", initCounter=" + initCounter + ", anyCounter=" + anyCounter
				+ ", endCounter=" + endCounter + ", sessionId=" + sessionId
				+ ", raImprint=" + raImprint + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anyCounter;
		result = prime * result + endCounter;
		result = prime * result + initCounter;
		result = prime * result + (int) (initTime ^ (initTime >>> 32));
		result = prime * result
				+ ((raImprint == null) ? 0 : raImprint.hashCode());
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageActivityImpl other = (MessageActivityImpl) obj;
		if (anyCounter != other.anyCounter)
			return false;
		if (endCounter != other.endCounter)
			return false;
		if (initCounter != other.initCounter)
			return false;
		if (initTime != other.initTime)
			return false;
		if (raImprint == null) {
			if (other.raImprint != null)
				return false;
		} else if (!raImprint.equals(other.raImprint))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (state != other.state)
			return false;
		return true;
	}
    
    
    
}
