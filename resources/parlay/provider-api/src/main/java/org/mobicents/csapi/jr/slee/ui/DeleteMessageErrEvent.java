package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request for deleting a message was not successful.
 * 
 * 
 */
public class DeleteMessageErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DeleteMessageErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DeleteMessageErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , org.csapi.ui.TpUIError error , int assignmentID ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.error = error;
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
     * Returns the error
     * 
     */
    public org.csapi.ui.TpUIError getError() {
        return this.error;
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
        if(!(o instanceof DeleteMessageErrEvent)) {
            return false;
        } 
        DeleteMessageErrEvent deleteMessageErrEvent = (DeleteMessageErrEvent) o;
        if(!(this.getService() == deleteMessageErrEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (deleteMessageErrEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(deleteMessageErrEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.error != null) && (deleteMessageErrEvent.error != null)) {
            if(!(this.error.equals(deleteMessageErrEvent.error)))  {
                return false;
            }
        }
        if(!(this.assignmentID == deleteMessageErrEvent.assignmentID)) {
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
    private org.csapi.ui.TpUIError error = null;
    private int assignmentID;

} // DeleteMessageErrEvent

