package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the network has detected that the overload has ceased and has automatically removed any load controls on calls requested to a particular address range or calls made to a particular destination within the call control service.
 * 
 * 
 */
public class CallOverloadCeasedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallOverloadCeasedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallOverloadCeasedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentID ){
        super(tpServiceIdentifier);
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the assignmentID
     * 
     */
    public int getAssignmentID() {
        return this.assignmentID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof CallOverloadCeasedEvent)) {
            return false;
        } 
        CallOverloadCeasedEvent callOverloadCeasedEvent = (CallOverloadCeasedEvent) o;
        if(!(this.getService() == callOverloadCeasedEvent.getService())) {
            return false;
        }
        if(!(this.assignmentID == callOverloadCeasedEvent.assignmentID)) {
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

    private int assignmentID;

} // CallOverloadCeasedEvent

