package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DirectDebitUnitResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DirectDebitUnitResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DirectDebitUnitResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpVolume[] debitedVolumes , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.debitedVolumes = debitedVolumes;
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
     * Returns the debitedVolumes
     * 
     */
    public org.csapi.cs.TpVolume[] getDebitedVolumes() {
        return this.debitedVolumes;
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
        if(!(o instanceof DirectDebitUnitResEvent)) {
            return false;
        } 
        DirectDebitUnitResEvent directDebitUnitResEvent = (DirectDebitUnitResEvent) o;
        if(!(this.getService() == directDebitUnitResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (directDebitUnitResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(directDebitUnitResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == directDebitUnitResEvent.requestNumber)) {
            return false;
        }
        if ((this.debitedVolumes != null) && (directDebitUnitResEvent.debitedVolumes != null)) {
            if(!(this.debitedVolumes.equals(directDebitUnitResEvent.debitedVolumes)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == directDebitUnitResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpVolume[] debitedVolumes = null;
    private int requestNumberNextRequest;

} // DirectDebitUnitResEvent

