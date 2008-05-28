package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DebitUnitResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DebitUnitResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DebitUnitResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpVolume[] debitedVolumes , org.csapi.cs.TpVolume[] reservedUnitsLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.debitedVolumes = debitedVolumes;
        this.reservedUnitsLeft = reservedUnitsLeft;
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
     * Returns the reservedUnitsLeft
     * 
     */
    public org.csapi.cs.TpVolume[] getReservedUnitsLeft() {
        return this.reservedUnitsLeft;
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
        if(!(o instanceof DebitUnitResEvent)) {
            return false;
        } 
        DebitUnitResEvent debitUnitResEvent = (DebitUnitResEvent) o;
        if(!(this.getService() == debitUnitResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (debitUnitResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(debitUnitResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == debitUnitResEvent.requestNumber)) {
            return false;
        }
        if ((this.debitedVolumes != null) && (debitUnitResEvent.debitedVolumes != null)) {
            if(!(this.debitedVolumes.equals(debitUnitResEvent.debitedVolumes)))  {
                return false;
            }
        }
        if ((this.reservedUnitsLeft != null) && (debitUnitResEvent.reservedUnitsLeft != null)) {
            if(!(this.reservedUnitsLeft.equals(debitUnitResEvent.reservedUnitsLeft)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == debitUnitResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpVolume[] reservedUnitsLeft = null;
    private int requestNumberNextRequest;

} // DebitUnitResEvent

