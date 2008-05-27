/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */

package org.mobicents.slee.resource;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;

import javax.slee.EventTypeID;

/**
 * 
 * This class provides the methods used by the resource adaptor
 * to retrieve the eventID by providing name, vendor, version of
 * the event
 * 
 * @author F.Moggia
 * 
 */
public class EventLookup {
	
	private static Logger logger = Logger.getLogger(EventLookup.class);
	
    SleeContainer container;
    
    public EventLookup(SleeContainer container) {
        this.container = container;
    }
    
    public String getEventClass() {
        return null;
    }
    
    public ClassLoader getEventClassLoader(int eventID) {
        return null;
        
    }
    
    public int getEventID(String name, String vendor, String version) {
    	if ( logger.isDebugEnabled()) {
    		logger.debug("getEventID(): name = " + name + " vendor = " + vendor + " version = " + version);
    	}
        EventTypeIDImpl eventID = (EventTypeIDImpl) container.getEventType(new ComponentKey(name, vendor, version));
        if (eventID == null) return -1;
        else return eventID.getEventID();
    }
    
    public String[] getEventType(int eventID) {
        
        return null;
    }
    
    public EventTypeID getEventTypeID(int eventID){
        return container.getEventTypeID(eventID);
    }

    
    public int  getEventID(ComponentKey key) {
        EventTypeIDImpl eventID = (EventTypeIDImpl) container.getEventType(key);
        return eventID.getEventID();
    }
}
