/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

/*
 * Created on Aug 7, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.resource;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;

/**
 * @author fmoggia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventLookupFacilityImpl implements EventLookupFacility{
    SleeContainer container;
    
    public EventLookupFacilityImpl(SleeContainer container) {
        this.container = container;
    }
    
    public String getEventClass() {
        return null;
    }
    
    public ClassLoader getEventClassLoader(int eventID) {
        return null;
        
    }
    
    public int getEventID(String name, String vendor, String version) {
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
    
    /*
     * NOT IMPLEMENTED YET
     */
    public String getEventClassName(int arg0) throws UnrecognizedEventException, FacilityException {
        // TODO Auto-generated method stub
        return null;
    }
}
