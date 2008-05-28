package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class CreditAmountResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CreditAmountResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CreditAmountResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingPrice creditedAmount , org.csapi.cs.TpChargingPrice reservedAmountLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.creditedAmount = creditedAmount;
        this.reservedAmountLeft = reservedAmountLeft;
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
     * Returns the creditedAmount
     * 
     */
    public org.csapi.cs.TpChargingPrice getCreditedAmount() {
        return this.creditedAmount;
    }
    /**
     * Returns the reservedAmountLeft
     * 
     */
    public org.csapi.cs.TpChargingPrice getReservedAmountLeft() {
        return this.reservedAmountLeft;
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
        if(!(o instanceof CreditAmountResEvent)) {
            return false;
        } 
        CreditAmountResEvent creditAmountResEvent = (CreditAmountResEvent) o;
        if(!(this.getService() == creditAmountResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (creditAmountResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(creditAmountResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == creditAmountResEvent.requestNumber)) {
            return false;
        }
        if ((this.creditedAmount != null) && (creditAmountResEvent.creditedAmount != null)) {
            if(!(this.creditedAmount.equals(creditAmountResEvent.creditedAmount)))  {
                return false;
            }
        }
        if ((this.reservedAmountLeft != null) && (creditAmountResEvent.reservedAmountLeft != null)) {
            if(!(this.reservedAmountLeft.equals(creditAmountResEvent.reservedAmountLeft)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == creditAmountResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpChargingPrice creditedAmount = null;
    private org.csapi.cs.TpChargingPrice reservedAmountLeft = null;
    private int requestNumberNextRequest;

} // CreditAmountResEvent

