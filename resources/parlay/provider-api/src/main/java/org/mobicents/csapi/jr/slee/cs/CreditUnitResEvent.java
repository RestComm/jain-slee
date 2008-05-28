package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class CreditUnitResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CreditUnitResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CreditUnitResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpVolume[] creditedVolumes , org.csapi.cs.TpVolume[] reservedUnitsLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.creditedVolumes = creditedVolumes;
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
     * Returns the creditedVolumes
     * 
     */
    public org.csapi.cs.TpVolume[] getCreditedVolumes() {
        return this.creditedVolumes;
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
        if(!(o instanceof CreditUnitResEvent)) {
            return false;
        } 
        CreditUnitResEvent creditUnitResEvent = (CreditUnitResEvent) o;
        if(!(this.getService() == creditUnitResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (creditUnitResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(creditUnitResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == creditUnitResEvent.requestNumber)) {
            return false;
        }
        if ((this.creditedVolumes != null) && (creditUnitResEvent.creditedVolumes != null)) {
            if(!(this.creditedVolumes.equals(creditUnitResEvent.creditedVolumes)))  {
                return false;
            }
        }
        if ((this.reservedUnitsLeft != null) && (creditUnitResEvent.reservedUnitsLeft != null)) {
            if(!(this.reservedUnitsLeft.equals(creditUnitResEvent.reservedUnitsLeft)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == creditUnitResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpVolume[] reservedUnitsLeft = null;
    private int requestNumberNextRequest;

} // CreditUnitResEvent

