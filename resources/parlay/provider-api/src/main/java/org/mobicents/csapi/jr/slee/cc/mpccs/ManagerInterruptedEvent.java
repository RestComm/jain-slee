package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that event notifications and method invocations have been temporarily interrupted (for example, due to network resources unavailable).
Note that more permanent failures are reported via the Framework (integrity management).
 * 
 * 
 */
public class ManagerInterruptedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ManagerInterruptedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ManagerInterruptedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier){
        super(tpServiceIdentifier);
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ManagerInterruptedEvent)) {
            return false;
        } 
        ManagerInterruptedEvent managerInterruptedEvent = (ManagerInterruptedEvent) o;
        if(!(this.getService() == managerInterruptedEvent.getService())) {
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


} // ManagerInterruptedEvent

