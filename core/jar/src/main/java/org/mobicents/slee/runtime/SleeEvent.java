/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */

package org.mobicents.slee.runtime;


import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;

/**
 * 
 * Slee Event Object Interface.
 * 
 * @author M. Ranganathan 
 * 
 * 
 */
public interface SleeEvent {
    public static final String ACTIVITY_EVENT = "ActivityEvent";

    /**
     * Get the activity context that encapsulates this event.
     */

    public String getActivityContextID();
  
    /**
     * Get the event type.
     */
    public EventTypeID getEventTypeID();
    /**
     * Get the event name.
     */
    
    public Object getEventObject();
    
    /**
     * get the address associated with this event.
     */
    public Address getAddress();

    /**
     * @return the stored ACI
     */
    public ActivityContextInterface getActivityContextInterface();
    
    /**
     * Set the ACI before routing event to the sbb.
     * This is a temporary place holder
     *
     */
    public void setActivityContextInterface( ActivityContextInterface aci);
    
    
    public Object getActivity();
    
    
   
    

}
