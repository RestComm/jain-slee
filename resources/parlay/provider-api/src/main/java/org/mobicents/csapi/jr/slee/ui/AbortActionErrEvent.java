package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to abort a user interaction operation on a call leg resulted in an error. 
 * 
 * 
 */
public class AbortActionErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for AbortActionErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public AbortActionErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , int assignmentID , org.csapi.ui.TpUIError error ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.assignmentID = assignmentID;
        this.error = error;
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
     * Returns the error
     * 
     */
    public org.csapi.ui.TpUIError getError() {
        return this.error;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof AbortActionErrEvent)) {
            return false;
        } 
        AbortActionErrEvent abortActionErrEvent = (AbortActionErrEvent) o;
        if(!(this.getService() == abortActionErrEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (abortActionErrEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(abortActionErrEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == abortActionErrEvent.assignmentID)) {
            return false;
        }
        if ((this.error != null) && (abortActionErrEvent.error != null)) {
            if(!(this.error.equals(abortActionErrEvent.error)))  {
                return false;
            }
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
    private org.csapi.ui.TpUIError error = null;

} // AbortActionErrEvent

