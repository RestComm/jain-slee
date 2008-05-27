
/*
 * Created on Jul 8, 2004
 *
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.resource.ResourceAdaptor;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

/** 
 * Slee Event wrapper. This is delivered to the SbbEntity. The sbb entity records
 * any victim Sbbs that have generated runtime exceptions here.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 */
public class SleeEventImpl implements SleeEvent  {
    
    private ArrayList errorSbbObjects;
    
    private EventTypeID eventID;

    private String activityContextId;
    
    private String resourceAdaptorEntityName;
    
    private ActivityContextInterface aci;
    
    
    

    private Object activity;

    private Object eventObject;
    
    private Address address;
    
    public String toString() {
        return new StringBuffer().append("SleeEventImpl.toString() = { ").
        append("\n eventID = " + eventID).
        append("\n activitycontext  = " + activityContextId).
        append("\n eventObject = " + eventObject).
        append("\n address = " + ( address != null ? address.getAddressString() : null )).
        append("\n activity = " + activity).
        append("}").toString();
        
    }

    
    public boolean hasGarbage() { return errorSbbObjects.size() != 0 ; }
    
    public SleeEventImpl(int eventID, Object event, String activityContextId,  Object activity, Address addr) {
        this.errorSbbObjects = new ArrayList();
        
        this.activityContextId = activityContextId;
        this.eventID = SleeContainer.lookupFromJndi().getEventTypeID(eventID);
        this.eventObject = event;
        this.address = addr;
        
        this.activity = activity;
      
    }
    
    
    public SleeEventImpl(EventTypeID eventTypeId, Object event, String activityContextId, Object activity, Address addr) {
        this.errorSbbObjects = new ArrayList();
        this.activityContextId = activityContextId;
        this.activity = activity;
        this.eventID = eventTypeId;
        this.eventObject = event;
        this.address = addr;
    }

    /*
    public ActivityContext getActivityContext() {
        return activityContext;
    }*/
    
    /*public ActivityContext getActivityContext() {
        return activityContext;
    }*/
    
    
    public Object getActivity(){
        return this.activity;
    }
    public String getActivityContextID() {
        return activityContextId;
    }
    
    
    //public Object getActivity() { return activity; }
   
    public EventTypeID getEventTypeID() {
        return eventID;
    }

    
    public Object getEventObject() {
        return this.eventObject;
    }

    
    public Address getAddress() {
        return this.address;
       
    }
    
    
    


    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#getActivityContextInterface()
     */
    public ActivityContext getActivityContext() {
        return SleeContainer.lookupFromJndi().getActivityContextFactory().getActivityContextById(activityContextId);
       
     
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.SleeEvent#setActivityContextInterface(javax.slee.ActivityContextInterface)
     */
    public void setActivityContextInterface(ActivityContextInterface aci) {
        this.aci = aci;
        
    }

   
    public  ActivityContextInterface getActivityContextInterface() {
        return this.aci;
    }

}
