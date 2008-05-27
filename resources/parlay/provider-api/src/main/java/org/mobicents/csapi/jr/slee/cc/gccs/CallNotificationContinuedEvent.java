package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that event notifications will again be possible.
 * 
 * 
 */
public class CallNotificationContinuedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallNotificationContinuedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallNotificationContinuedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallNotificationContinuedEvent)) {
            return false;
        } 
        CallNotificationContinuedEvent callNotificationContinuedEvent = (CallNotificationContinuedEvent) o;
        if(!(this.getService() == callNotificationContinuedEvent.getService())) {
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


} // CallNotificationContinuedEvent

