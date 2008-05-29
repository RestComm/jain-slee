package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the charging session object (at the gateway) has aborted or terminated abnormally. No further communication will be possible between the charging session and application. 
 * 
 * 
 */
public class SessionAbortedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SessionAbortedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SessionAbortedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SessionAbortedEvent)) {
            return false;
        } 
        SessionAbortedEvent sessionAbortedEvent = (SessionAbortedEvent) o;
        if(!(this.getService() == sessionAbortedEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (sessionAbortedEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(sessionAbortedEvent.tpChargingSessionID)))  {
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

} // SessionAbortedEvent

