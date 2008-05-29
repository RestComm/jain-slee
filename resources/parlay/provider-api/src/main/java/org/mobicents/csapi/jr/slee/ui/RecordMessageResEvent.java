package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event returns whether the message is successfully recorded or not. In case the message is recorded, the ID of the message is returned. 
 * 
 * 
 */
public class RecordMessageResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RecordMessageResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RecordMessageResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier , int assignmentID , org.csapi.ui.TpUIReport response , int messageID ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.assignmentID = assignmentID;
        this.response = response;
        this.messageID = messageID;
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
     * Returns the response
     * 
     */
    public org.csapi.ui.TpUIReport getResponse() {
        return this.response;
    }
    /**
     * Returns the messageID
     * 
     */
    public int getMessageID() {
        return this.messageID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof RecordMessageResEvent)) {
            return false;
        } 
        RecordMessageResEvent recordMessageResEvent = (RecordMessageResEvent) o;
        if(!(this.getService() == recordMessageResEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (recordMessageResEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(recordMessageResEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == recordMessageResEvent.assignmentID)) {
            return false;
        }
        if ((this.response != null) && (recordMessageResEvent.response != null)) {
            if(!(this.response.equals(recordMessageResEvent.response)))  {
                return false;
            }
        }
        if(!(this.messageID == recordMessageResEvent.messageID)) {
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
    private org.csapi.ui.TpUIReport response = null;
    private int messageID;

} // RecordMessageResEvent

