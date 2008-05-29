package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class DirectCreditAmountResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DirectCreditAmountResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DirectCreditAmountResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingPrice creditedAmount , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.creditedAmount = creditedAmount;
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
        if(!(o instanceof DirectCreditAmountResEvent)) {
            return false;
        } 
        DirectCreditAmountResEvent directCreditAmountResEvent = (DirectCreditAmountResEvent) o;
        if(!(this.getService() == directCreditAmountResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (directCreditAmountResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(directCreditAmountResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == directCreditAmountResEvent.requestNumber)) {
            return false;
        }
        if ((this.creditedAmount != null) && (directCreditAmountResEvent.creditedAmount != null)) {
            if(!(this.creditedAmount.equals(directCreditAmountResEvent.creditedAmount)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == directCreditAmountResEvent.requestNumberNextRequest)) {
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
    private int requestNumberNextRequest;

} // DirectCreditAmountResEvent

