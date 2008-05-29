package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DebitAmountResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DebitAmountResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DebitAmountResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingPrice debitedAmount , org.csapi.cs.TpChargingPrice reservedAmountLeft , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.debitedAmount = debitedAmount;
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
     * Returns the debitedAmount
     * 
     */
    public org.csapi.cs.TpChargingPrice getDebitedAmount() {
        return this.debitedAmount;
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
        if(!(o instanceof DebitAmountResEvent)) {
            return false;
        } 
        DebitAmountResEvent debitAmountResEvent = (DebitAmountResEvent) o;
        if(!(this.getService() == debitAmountResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (debitAmountResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(debitAmountResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == debitAmountResEvent.requestNumber)) {
            return false;
        }
        if ((this.debitedAmount != null) && (debitAmountResEvent.debitedAmount != null)) {
            if(!(this.debitedAmount.equals(debitAmountResEvent.debitedAmount)))  {
                return false;
            }
        }
        if ((this.reservedAmountLeft != null) && (debitAmountResEvent.reservedAmountLeft != null)) {
            if(!(this.reservedAmountLeft.equals(debitAmountResEvent.reservedAmountLeft)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == debitAmountResEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpChargingPrice debitedAmount = null;
    private org.csapi.cs.TpChargingPrice reservedAmountLeft = null;
    private int requestNumberNextRequest;

} // DebitAmountResEvent

