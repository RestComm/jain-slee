package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the User Interaction service instance has terminated or closed abnormally. No further communication will be possible between the User Interaction service instance and application. 
 * 
 * 
 */
public class UserInteractionAbortedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{



    /**
     * Constructor for UserInteractionAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     * @param userInteraction Generic User Interaction this event is related to
     */
    public UserInteractionAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUIIdentifier genericUserInteraction ){
        super(tpServiceIdentifier);
        this.genericUserInteraction = genericUserInteraction;
    }

    /**
     * Constructor for UserInteractionAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     * @param userInteraction Call Related User Interaction this event is related to
     */
    public UserInteractionAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier callUserInteraction ){
        super(tpServiceIdentifier);
        this.callUserInteraction = callUserInteraction;
    }
    
    
    /**
     * Returns the Generic User Interaction
     * 
     */
    public TpUIIdentifier getGenericUserInteraction() {
        return this.genericUserInteraction;
    }

    /**
     * Returns the Call related User Interaction
     * 
     */
    public TpUICallIdentifier getCallUserInteraction() {
        return this.callUserInteraction;
    }
    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof UserInteractionAbortedEvent)) {
            return false;
        } 
        UserInteractionAbortedEvent userInteractionAbortedEvent = (UserInteractionAbortedEvent) o;
        if(!(this.getService() == userInteractionAbortedEvent.getService())) {
            return false;
        }
        if ((this.genericUserInteraction != null) && (userInteractionAbortedEvent.genericUserInteraction != null)) {
            if(!(this.genericUserInteraction.equals(userInteractionAbortedEvent.genericUserInteraction)))  {
                return false;
            }
        }
        if ((this.callUserInteraction != null) && (userInteractionAbortedEvent.callUserInteraction != null)) {
            if(!(this.callUserInteraction.equals(userInteractionAbortedEvent.callUserInteraction)))  {
                return false;
            }
        }        
        if (this.hashCode() != o.hashCode()) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hashcode value for the object.
     */
    public int hashCode() {
        return 1;
    }

    // VARIABLES
    // .......................................................

    private TpUIIdentifier genericUserInteraction = null;
    private TpUICallIdentifier callUserInteraction = null;
} // UserInteractionAbortedEvent

