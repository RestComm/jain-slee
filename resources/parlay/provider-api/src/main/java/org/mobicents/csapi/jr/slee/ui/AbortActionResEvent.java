package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event confirms that the request to abort a user interaction operation on a call leg was successful. 
 * 
 * 
 */
public class AbortActionResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for AbortActionResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public AbortActionResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , int assignmentID ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the tpUICallIdentifier
     * 
     */
    public TpUICallIdentifier getTpUICallIdentifier() {
        return this.tpUICallIdentifier;
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
        if(!(o instanceof AbortActionResEvent)) {
            return false;
        } 
        AbortActionResEvent abortActionResEvent = (AbortActionResEvent) o;
        if(!(this.getService() == abortActionResEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (abortActionResEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(abortActionResEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == abortActionResEvent.assignmentID)) {
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

    private TpUICallIdentifier tpUICallIdentifier = null;
    private int assignmentID;

} // AbortActionResEvent

