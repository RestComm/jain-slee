package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that event notifications are possible and method invocations are enabled.
 * 
 * 
 */
public class ManagerResumedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ManagerResumedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ManagerResumedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ManagerResumedEvent)) {
            return false;
        } 
        ManagerResumedEvent managerResumedEvent = (ManagerResumedEvent) o;
        if(!(this.getService() == managerResumedEvent.getService())) {
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


} // ManagerResumedEvent

