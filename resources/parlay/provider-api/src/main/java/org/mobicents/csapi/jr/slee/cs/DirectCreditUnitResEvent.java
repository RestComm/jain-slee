package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DirectCreditUnitResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DirectCreditUnitResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DirectCreditUnitResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpVolume[] creditedVolumes , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.creditedVolumes = creditedVolumes;
        this.requestNumberNextRequest = requestNumberNextRequest;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }
    /**
     * Returns the requestNumber
     * 
     */
    public int getRequestNumber() {
        return this.requestNumber;
    }
    /**
     * Returns the creditedVolumes
     * 
     */
    public org.csapi.cs.TpVolume[] getCreditedVolumes() {
        return this.creditedVolumes;
    }
    /**
     * Returns the requestNumberNextRequest
     * 
     */
    public int getRequestNumberNextRequest() {
        return this.requestNumberNextRequest;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof DirectCreditUnitResEvent)) {
            return false;
        } 
        DirectCreditUnitResEvent directCreditUnitResEvent = (DirectCreditUnitResEvent) o;
        if(!(this.getService() == directCreditUnitResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (directCreditUnitResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(directCreditUnitResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == directCreditUnitResEvent.requestNumber)) {
            return false;
        }
        if ((this.creditedVolumes != null) && (directCreditUnitResEvent.creditedVolumes != null)) {
            if(!(this.creditedVolumes.equals(directCreditUnitResEvent.creditedVolumes)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == directCreditUnitResEvent.requestNumberNextRequest)) {
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

    private TpChargingSessionID tpChargingSessionID = null;
    private int requestNumber;
    private org.csapi.cs.TpVolume[] creditedVolumes = null;
    private int requestNumberNextRequest;

} // DirectCreditUnitResEvent

