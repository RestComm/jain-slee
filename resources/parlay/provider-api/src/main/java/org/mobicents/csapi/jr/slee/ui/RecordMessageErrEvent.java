package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request for recording of a message was not successful. 
 * 
 * 
 */
public class RecordMessageErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RecordMessageErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RecordMessageErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , int assignmentID , org.csapi.ui.TpUIError error ){
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
        if(!(o instanceof RecordMessageErrEvent)) {
            return false;
        } 
        RecordMessageErrEvent recordMessageErrEvent = (RecordMessageErrEvent) o;
        if(!(this.getService() == recordMessageErrEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (recordMessageErrEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(recordMessageErrEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == recordMessageErrEvent.assignmentID)) {
            return false;
        }
        if ((this.error != null) && (recordMessageErrEvent.error != null)) {
            if(!(this.error.equals(recordMessageErrEvent.error)))  {
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

} // RecordMessageErrEvent

