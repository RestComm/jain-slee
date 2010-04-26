package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.facilities.TimerID;

/**
 * An implementation of timer ID.
 *
 */
public class TimerIDImpl implements TimerID, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -230916225922881179L;
	private String id;
    
	TimerIDImpl(String id) {
        this.id = id;       
    }
    
	public boolean equals(Object obj) {
    	if ((obj != null) && (obj.getClass() == this.getClass())) {
    		return this.id.equals(((TimerIDImpl)obj).id);
    	}
    	else {
    		return false;
    	}
    }
    
    public int hashCode() { return id.hashCode(); }
    public String toString() { return id; }
}

