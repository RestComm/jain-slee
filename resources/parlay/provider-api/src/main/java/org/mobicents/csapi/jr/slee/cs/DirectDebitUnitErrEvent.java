package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request failed completely and that no units have been debited. 
 * 
 * 
 */
public class DirectDebitUnitErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for DirectDebitUnitErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public DirectDebitUnitErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int requestNumber , org.csapi.cs.TpChargingError error , int requestNumberNextRequest ){
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
        if(!(o instanceof DirectDebitUnitErrEvent)) {
            return false;
        } 
        DirectDebitUnitErrEvent directDebitUnitErrEvent = (DirectDebitUnitErrEvent) o;
        if(!(this.getService() == directDebitUnitErrEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (directDebitUnitErrEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(directDebitUnitErrEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.requestNumber == directDebitUnitErrEvent.requestNumber)) {
            return false;
        }
        if ((this.error != null) && (directDebitUnitErrEvent.error != null)) {
            if(!(this.error.equals(directDebitUnitErrEvent.error)))  {
                return false;
            }
        }
        if(!(this.requestNumberNextRequest == directDebitUnitErrEvent.requestNumberNextRequest)) {
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

} // DirectDebitUnitErrEvent

