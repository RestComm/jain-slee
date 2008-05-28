package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that event notifications will again be possible.  
 * 
 * 
 */
public class UserInteractionNotificationContinuedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for UserInteractionNotificationContinuedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public UserInteractionNotificationContinuedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof UserInteractionNotificationContinuedEvent)) {
            return false;
        } 
        UserInteractionNotificationContinuedEvent userInteractionNotificationContinuedEvent = (UserInteractionNotificationContinuedEvent) o;
        if(!(this.getService() == userInteractionNotificationContinuedEvent.getService())) {
            return false;
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


} // UserInteractionNotificationContinuedEvent

