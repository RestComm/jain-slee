package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request failed. 
 * 
 * 
 */
public class ExtendLifeTimeErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ExtendLifeTimeErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ExtendLifeTimeErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , org.csapi.cs.TpChargingError error ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.error = error;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }
    /**
     * Returns the error
     * 
     */
    public org.csapi.cs.TpChargingError getError() {
        return this.error;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ExtendLifeTimeErrEvent)) {
            return false;
        } 
        ExtendLifeTimeErrEvent extendLifeTimeErrEvent = (ExtendLifeTimeErrEvent) o;
        if(!(this.getService() == extendLifeTimeErrEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (extendLifeTimeErrEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(extendLifeTimeErrEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if ((this.error != null) && (extendLifeTimeErrEvent.error != null)) {
            if(!(this.error.equals(extendLifeTimeErrEvent.error)))  {
                return false;
            }
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
    private org.csapi.cs.TpChargingError error = null;

} // ExtendLifeTimeErrEvent

