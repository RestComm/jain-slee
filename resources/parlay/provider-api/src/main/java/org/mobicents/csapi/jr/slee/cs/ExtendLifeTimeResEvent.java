package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the corresponding request was successful.
 * 
 * 
 */
public class ExtendLifeTimeResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ExtendLifeTimeResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ExtendLifeTimeResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , int sessionTimeLeft ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.sessionTimeLeft = sessionTimeLeft;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }
    /**
     * Returns the sessionTimeLeft
     * 
     */
    public int getSessionTimeLeft() {
        return this.sessionTimeLeft;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ExtendLifeTimeResEvent)) {
            return false;
        } 
        ExtendLifeTimeResEvent extendLifeTimeResEvent = (ExtendLifeTimeResEvent) o;
        if(!(this.getService() == extendLifeTimeResEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (extendLifeTimeResEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(extendLifeTimeResEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if(!(this.sessionTimeLeft == extendLifeTimeResEvent.sessionTimeLeft)) {
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
    private int sessionTimeLeft;

} // ExtendLifeTimeResEvent

