package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that all event notifications are resumed.
 * 
 * 
 */
public class DataSessionNotificationContinuedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DataSessionNotificationContinuedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DataSessionNotificationContinuedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DataSessionNotificationContinuedEvent)) {
            return false;
        } 
        DataSessionNotificationContinuedEvent dataSessionNotificationContinuedEvent = (DataSessionNotificationContinuedEvent) o;
        if(!(this.getService() == dataSessionNotificationContinuedEvent.getService())) {
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


} // DataSessionNotificationContinuedEvent

