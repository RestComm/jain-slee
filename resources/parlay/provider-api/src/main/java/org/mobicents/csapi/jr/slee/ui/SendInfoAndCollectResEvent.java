package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event returns the information collected to the application. 
 * 
 * 
 */
public class SendInfoAndCollectResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SendInfoAndCollectResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SendInfoAndCollectResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUICallIdentifier tpUICallIdentifier, TpUIIdentifier tpUIIdentifier , int assignmentID , org.csapi.ui.TpUIReport response , String collectedInfo ){
        super(tpServiceIdentifier);
        this.tpUICallIdentifier = tpUICallIdentifier;
        this.tpUIIdentifier = tpUIIdentifier;
        this.assignmentID = assignmentID;
        this.response = response;
        this.collectedInfo = collectedInfo;
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
     * Returns the collectedInfo
     * 
     */
    public String getCollectedInfo() {
        return this.collectedInfo;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SendInfoAndCollectResEvent)) {
            return false;
        } 
        SendInfoAndCollectResEvent sendInfoAndCollectResEvent = (SendInfoAndCollectResEvent) o;
        if(!(this.getService() == sendInfoAndCollectResEvent.getService())) {
            return false;
        }
        if ((this.tpUICallIdentifier != null) && (sendInfoAndCollectResEvent.tpUICallIdentifier != null)) {
            if(!(this.tpUICallIdentifier.equals(sendInfoAndCollectResEvent.tpUICallIdentifier)))  {
                return false;
            }
        }
        if ((this.tpUIIdentifier != null) && (sendInfoAndCollectResEvent.tpUIIdentifier != null)) {
            if(!(this.tpUIIdentifier.equals(sendInfoAndCollectResEvent.tpUIIdentifier)))  {
                return false;
            }
        }
        if(!(this.assignmentID == sendInfoAndCollectResEvent.assignmentID)) {
            return false;
        }
        if ((this.response != null) && (sendInfoAndCollectResEvent.response != null)) {
            if(!(this.response.equals(sendInfoAndCollectResEvent.response)))  {
                return false;
            }
        }
        if ((this.collectedInfo != null) && (sendInfoAndCollectResEvent.collectedInfo != null)) {
            if(!(this.collectedInfo.equals(sendInfoAndCollectResEvent.collectedInfo)))  {
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
    private String collectedInfo = null;

} // SendInfoAndCollectResEvent

