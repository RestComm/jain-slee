package org.mobicents.csapi.jr.slee.gms;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that all event notifications have been terminated (for example, due to faults detected). 
 * 
 * 
 */
public class MessagingNotificationTerminatedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for MessagingNotificationTerminatedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public MessagingNotificationTerminatedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof MessagingNotificationTerminatedEvent)) {
            return false;
        } 
        MessagingNotificationTerminatedEvent messagingNotificationTerminatedEvent = (MessagingNotificationTerminatedEvent) o;
        if(!(this.getService() == messagingNotificationTerminatedEvent.getService())) {
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


} // MessagingNotificationTerminatedEvent

