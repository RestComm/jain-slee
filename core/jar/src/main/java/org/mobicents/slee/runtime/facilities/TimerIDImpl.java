/*
 * TimerIDImpl.java
 * 
 * Created on Jan 26, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

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

