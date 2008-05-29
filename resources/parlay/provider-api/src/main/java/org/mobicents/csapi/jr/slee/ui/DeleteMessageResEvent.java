package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event returns whether the message is successfully deleted or not.
 * 
 * 
 */
public class DeleteMessageResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DeleteMessageResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DeleteMessageResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , org.csapi.ui.TpUIReport response , int assignmentID ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.response = response;
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
     * Returns the response
     * 
     */
    public org.csapi.ui.TpUIReport getResponse() {
        return this.response;
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
        if(!(o instanceof DeleteMessageResEvent)) {
            return false;
        } 
        DeleteMessageResEvent deleteMessageResEvent = (DeleteMessageResEvent) o;
        if(!(this.getService() == deleteMessageResEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (deleteMessageResEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(deleteMessageResEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.response != null) && (deleteMessageResEvent.response != null)) {
            if(!(this.response.equals(deleteMessageResEvent.response)))  {
                return false;
            }
        }
        if(!(this.assignmentID == deleteMessageResEvent.assignmentID)) {
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
    private org.csapi.ui.TpUIReport response = null;
    private int assignmentID;

} // DeleteMessageResEvent

