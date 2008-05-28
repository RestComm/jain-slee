package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class ReserveAmountResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReserveAmountResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReserveAmountResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingPrice reservedAmount , int sessionTimeLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.reservedAmount = reservedAmount;
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
     * Returns the reservedAmount
     * 
     */
    public org.csapi.cs.TpChargingPrice getReservedAmount() {
        return this.reservedAmount;
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
        if(!(o instanceof ReserveAmountResEvent)) {
            return false;
        } 
        ReserveAmountResEvent reserveAmountResEvent = (ReserveAmountResEvent) o;
        if(!(this.getService() == reserveAmountResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (reserveAmountResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(reserveAmountResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == reserveAmountResEvent.requestNumber)) {
            return false;
        }
        if ((this.reservedAmount != null) && (reserveAmountResEvent.reservedAmount != null)) {
            if(!(this.reservedAmount.equals(reserveAmountResEvent.reservedAmount)))  {
                return false;
            }
        }
        if(!(this.sessionTimeLeft == reserveAmountResEvent.sessionTimeLeft)) {
            return false;
        }
        if(!(this.requestNumberNextRequest == reserveAmountResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpChargingPrice reservedAmount = null;
    private int sessionTimeLeft;
    private int requestNumberNextRequest;

} // ReserveAmountResEvent

