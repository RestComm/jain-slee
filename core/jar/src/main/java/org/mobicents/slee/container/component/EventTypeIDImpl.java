/*
* Created on Aug 5, 2004
*
*The Open SLEE project
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*
*/

package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.slee.EventTypeID;


/**
 * Implementation of event type ID. This is filled in by the parser.
 * 
 * 
 * @author M. Ranganathan
 *  
 */
public class EventTypeIDImpl  extends ComponentIDImpl implements Serializable,EventTypeID, Comparable {
	
    private static AtomicInteger typeCounter = new AtomicInteger(0); 
	
	private int typeId;
	
    public EventTypeIDImpl(ComponentKey componentKey ) {
       super( componentKey);
       this.typeId = typeCounter.getAndIncrement();
    }
    
    public int getEventID(){
        return typeId;
    }

    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		return ((EventTypeIDImpl)obj).typeId == this.typeId;
    	}
    	else {
    		return false;
    	}           
    }

    public int hashCode() {
    	return typeId;
    }
    
    public String toString() {
        return super.toString() + ", #" + typeId;
    }

	public int compareTo(Object arg0) {
		if( !(arg0 instanceof EventTypeIDImpl))
			return 1;
		
		EventTypeIDImpl eventTypeID=(EventTypeIDImpl)arg0;
		
		return this.typeId-eventTypeID.typeId;
	}

}
