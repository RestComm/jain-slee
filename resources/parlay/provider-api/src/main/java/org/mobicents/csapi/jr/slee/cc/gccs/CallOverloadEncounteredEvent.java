package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the network has detected overload and may have automatically imposed load control on calls requested to a particular address range or calls made to a particular destination within the call control service.
 * 
 * 
 */
public class CallOverloadEncounteredEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallOverloadEncounteredEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallOverloadEncounteredEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentID ){
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
        if(!(o instanceof CallOverloadEncounteredEvent)) {
            return false;
        } 
        CallOverloadEncounteredEvent callOverloadEncounteredEvent = (CallOverloadEncounteredEvent) o;
        if(!(this.getService() == callOverloadEncounteredEvent.getService())) {
            return false;
        }
        if(!(this.assignmentID == callOverloadEncounteredEvent.assignmentID)) {
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

} // CallOverloadEncounteredEvent

