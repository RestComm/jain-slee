package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class RateResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RateResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RateResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , org.csapi.cs.TpPriceVolume[] rates , int validityTimeLeft ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.rates = rates;
        this.validityTimeLeft = validityTimeLeft;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }
    /**
     * Returns the rates
     * 
     */
    public org.csapi.cs.TpPriceVolume[] getRates() {
        return this.rates;
    }
    /**
     * Returns the validityTimeLeft
     * 
     */
    public int getValidityTimeLeft() {
        return this.validityTimeLeft;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof RateResEvent)) {
            return false;
        } 
        RateResEvent rateResEvent = (RateResEvent) o;
        if(!(this.getService() == rateResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (rateResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(rateResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if ((this.rates != null) && (rateResEvent.rates != null)) {
            if(!(this.rates.equals(rateResEvent.rates)))  {
                return false;
            }
        }
        if(!(this.validityTimeLeft == rateResEvent.validityTimeLeft)) {
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
    private org.csapi.cs.TpPriceVolume[] rates = null;
    private int validityTimeLeft;

} // RateResEvent

