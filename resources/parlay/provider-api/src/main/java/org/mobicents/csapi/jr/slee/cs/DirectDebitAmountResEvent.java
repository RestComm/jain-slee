package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DirectDebitAmountResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DirectDebitAmountResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DirectDebitAmountResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingPrice debitedAmount , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.debitedAmount = debitedAmount;
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
        if(!(o instanceof DirectDebitAmountResEvent)) {
            return false;
        } 
        DirectDebitAmountResEvent directDebitAmountResEvent = (DirectDebitAmountResEvent) o;
        if(!(this.getService() == directDebitAmountResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (directDebitAmountResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(directDebitAmountResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == directDebitAmountResEvent.requestNumber)) {
            return false;
        }
        if ((this.debitedAmount != null) && (directDebitAmountResEvent.debitedAmount != null)) {
            if(!(this.debitedAmount.equals(directDebitAmountResEvent.debitedAmount)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == directDebitAmountResEvent.requestNumberNextRequest)) {
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
    private int requestNumberNextRequest;

} // DirectDebitAmountResEvent

