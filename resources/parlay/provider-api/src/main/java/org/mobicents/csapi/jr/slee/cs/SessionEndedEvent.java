package org.mobicents.csapi.jr.slee.cs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates to the application that the charging session has terminated in the charging server. The application is expected to deassign the charging session object after having received the sessionEnded.
 * 
 * 
 */
public class SessionEndedEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SessionEndedEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SessionEndedEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpChargingSessionID tpChargingSessionID , org.csapi.cs.TpSessionEndedCause report ){
        super(tpServiceIdentifier);
        this.tpChargingSessionID = tpChargingSessionID;
        this.report = report;
    }

    /**
     * Returns the tpChargingSessionID
     * 
     */
    public TpChargingSessionID getTpChargingSessionID() {
        return this.tpChargingSessionID;
    }
    /**
     * Returns the report
     * 
     */
    public org.csapi.cs.TpSessionEndedCause getReport() {
        return this.report;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SessionEndedEvent)) {
            return false;
        } 
        SessionEndedEvent sessionEndedEvent = (SessionEndedEvent) o;
        if(!(this.getService() == sessionEndedEvent.getService())) {
            return false;
        }
        if ((this.tpChargingSessionID != null) && (sessionEndedEvent.tpChargingSessionID != null)) {
            if(!(this.tpChargingSessionID.equals(sessionEndedEvent.tpChargingSessionID)))  {
                return false;
            }
        }
        if ((this.report != null) && (sessionEndedEvent.report != null)) {
            if(!(this.report.equals(sessionEndedEvent.report)))  {
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
    private org.csapi.cs.TpSessionEndedCause report = null;

} // SessionEndedEvent

