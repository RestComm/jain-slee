package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request failed. The reservation cannot be used.
 * 
 * 
 */
public class ReserveUnitErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReserveUnitErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReserveUnitErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingError error , int requestNumberNextRequest ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.requestNumber = requestNumber;
        this.error = error;
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
     * Returns the error
     * 
     */
    public org.csapi.cs.TpChargingError getError() {
        return this.error;
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
        if(!(o instanceof ReserveUnitErrEvent)) {
            return false;
        } 
        ReserveUnitErrEvent reserveUnitErrEvent = (ReserveUnitErrEvent) o;
        if(!(this.getService() == reserveUnitErrEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (reserveUnitErrEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(reserveUnitErrEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == reserveUnitErrEvent.requestNumber)) {
            return false;
        }
        if ((this.error != null) && (reserveUnitErrEvent.error != null)) {
            if(!(this.error.equals(reserveUnitErrEvent.error)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == reserveUnitErrEvent.requestNumberNextRequest)) {
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
    private org.csapi.cs.TpChargingError error = null;
    private int requestNumberNextRequest;

} // ReserveUnitErrEvent

