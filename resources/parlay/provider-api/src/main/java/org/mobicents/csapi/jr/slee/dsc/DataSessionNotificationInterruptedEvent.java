package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that event notifications will no longer be sent (for example, due to faults detected).
 * 
 * 
 */
public class DataSessionNotificationInterruptedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DataSessionNotificationInterruptedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DataSessionNotificationInterruptedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DataSessionNotificationInterruptedEvent)) {
            return false;
        } 
        DataSessionNotificationInterruptedEvent dataSessionNotificationInterruptedEvent = (DataSessionNotificationInterruptedEvent) o;
        if(!(this.getService() == dataSessionNotificationInterruptedEvent.getService())) {
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


} // DataSessionNotificationInterruptedEvent

