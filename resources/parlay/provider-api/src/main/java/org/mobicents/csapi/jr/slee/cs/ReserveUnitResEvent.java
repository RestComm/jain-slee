package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class ReserveUnitResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReserveUnitResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReserveUnitResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpVolume[] reservedUnits , int sessionTimeLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.reservedUnits = reservedUnits;
        this.sessionTimeLeft = sessionTimeLeft;
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
     * Returns the reservedUnits
     * 
     */
    public org.csapi.cs.TpVolume[] getReservedUnits() {
        return this.reservedUnits;
    }
    /**
     * Returns the sessionTimeLeft
     * 
     */
    public int getSessionTimeLeft() {
        return this.sessionTimeLeft;
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
        if(!(o instanceof ReserveUnitResEvent)) {
            return false;
        } 
        ReserveUnitResEvent reserveUnitResEvent = (ReserveUnitResEvent) o;
        if(!(this.getService() == reserveUnitResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (reserveUnitResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(reserveUnitResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == reserveUnitResEvent.requestNumber)) {
            return false;
        }
        if ((this.reservedUnits != null) && (reserveUnitResEvent.reservedUnits != null)) {
            if(!(this.reservedUnits.equals(reserveUnitResEvent.reservedUnits)))  {
                return false;
            }
        }
        if(!(this.sessionTimeLeft == reserveUnitResEvent.sessionTimeLeft)) {
            return false;
        }
        if(!(this.requestNumberNextRequest == reserveUnitResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpVolume[] reservedUnits = null;
    private int sessionTimeLeft;
    private int requestNumberNextRequest;

} // ReserveUnitResEvent

