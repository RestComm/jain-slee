package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event indicates that the request to send information was unsuccessful. This response is called only if the responseRequested parameter of the sendInfoReq() method was set to P_UI_RESPONSE_REQUIRED.In the event that a response was not requested and the user interaction was unsuccessful the implementation of the service capability must handle the network error, however the error shall not be reported to the application as it requested no response.
 * 
 * 
 */
public class SendInfoErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SendInfoErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SendInfoErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier, TpUIIdentifier tpUIIdentifier , int assignmentID , org.csapi.ui.TpUIError error ){
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
        if(!(o instanceof SendInfoErrEvent)) {
            return false;
        } 
        SendInfoErrEvent sendInfoErrEvent = (SendInfoErrEvent) o;
        if(!(this.getService() == sendInfoErrEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (sendInfoErrEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(sendInfoErrEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpUIIdentifier != null) && (sendInfoErrEvent.tpUIIdentifier != null)) {
            if(!(this.tpUIIdentifier.equals(sendInfoErrEvent.tpUIIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == sendInfoErrEvent.assignmentID)) {
            return false;
        }
        if ((this.error != null) && (sendInfoErrEvent.error != null)) {
            if(!(this.error.equals(sendInfoErrEvent.error)))  {
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

} // SendInfoErrEvent

