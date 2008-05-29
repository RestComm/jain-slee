package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event informs the application about the completion of a sendInfoReq(). This response is called only if the responseRequested parameter of the sendInfoReq() method was set to P_UI_RESPONSE_REQUIRED. 
 * 
 * 
 */
public class SendInfoResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SendInfoResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SendInfoResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier, TpUIIdentifier tpUIIdentifier , int assignmentID , org.csapi.ui.TpUIReport response ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.tpUIIdentifier = tpUIIdentifier;
        this.assignmentID = assignmentID;
        this.response = response;
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
     * Returns the response
     * 
     */
    public org.csapi.ui.TpUIReport getResponse() {
        return this.response;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SendInfoResEvent)) {
            return false;
        } 
        SendInfoResEvent sendInfoResEvent = (SendInfoResEvent) o;
        if(!(this.getService() == sendInfoResEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (sendInfoResEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(sendInfoResEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpUIIdentifier != null) && (sendInfoResEvent.tpUIIdentifier != null)) {
            if(!(this.tpUIIdentifier.equals(sendInfoResEvent.tpUIIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == sendInfoResEvent.assignmentID)) {
            return false;
        }
        if ((this.response != null) && (sendInfoResEvent.response != null)) {
            if(!(this.response.equals(sendInfoResEvent.response)))  {
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
    private org.csapi.ui.TpUIReport response = null;

} // SendInfoResEvent

