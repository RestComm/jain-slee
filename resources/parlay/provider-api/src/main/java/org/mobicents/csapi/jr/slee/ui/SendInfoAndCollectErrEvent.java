package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to send information and collect a response was unsuccessful. 
 * 
 * 
 */
public class SendInfoAndCollectErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SendInfoAndCollectErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SendInfoAndCollectErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier, TpUIIdentifier tpUIIdentifier , int assignmentID , org.csapi.ui.TpUIError error ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.tpUIIdentifier = tpUIIdentifier;
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
     * Returns the tpUIIdentifier
     * 
     */
    public TpUIIdentifier getTpUIIdentifier() {
        return this.tpUIIdentifier;
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
        if(!(o instanceof SendInfoAndCollectErrEvent)) {
            return false;
        } 
        SendInfoAndCollectErrEvent sendInfoAndCollectErrEvent = (SendInfoAndCollectErrEvent) o;
        if(!(this.getService() == sendInfoAndCollectErrEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (sendInfoAndCollectErrEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(sendInfoAndCollectErrEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpUIIdentifier != null) && (sendInfoAndCollectErrEvent.tpUIIdentifier != null)) {
            if(!(this.tpUIIdentifier.equals(sendInfoAndCollectErrEvent.tpUIIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == sendInfoAndCollectErrEvent.assignmentID)) {
            return false;
        }
        if ((this.error != null) && (sendInfoAndCollectErrEvent.error != null)) {
            if(!(this.error.equals(sendInfoAndCollectErrEvent.error)))  {
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
    private TpUIIdentifier tpUIIdentifier = null;
    private int assignmentID;
    private org.csapi.ui.TpUIError error = null;

} // SendInfoAndCollectErrEvent

