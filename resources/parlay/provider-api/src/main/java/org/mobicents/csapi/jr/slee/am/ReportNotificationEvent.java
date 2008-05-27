package org.mobicents.csapi.jr.slee.am;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event is used to notify the application of a charging event.
 * 
 * 
 */
public class ReportNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, org.csapi.am.TpChargingEventInfo chargingEventInfo , int assignmentId ){
        super(tpServiceIdentifier);
        this.chargingEventInfo = chargingEventInfo;
        this.assignmentId = assignmentId;
    }

    /**
     * Returns the chargingEventInfo
     * 
     */
    public org.csapi.am.TpChargingEventInfo getChargingEventInfo() {
        return this.chargingEventInfo;
    }
    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ReportNotificationEvent)) {
            return false;
        } 
        ReportNotificationEvent reportNotificationEvent = (ReportNotificationEvent) o;
        if(!(this.getService() == reportNotificationEvent.getService())) {
            return false;
        }
        if ((this.chargingEventInfo != null) && (reportNotificationEvent.chargingEventInfo != null)) {
            if(!(this.chargingEventInfo.equals(reportNotificationEvent.chargingEventInfo)))  {
                return false;
            }
        }
        if(!(this.assignmentId == reportNotificationEvent.assignmentId)) {
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

    private org.csapi.am.TpChargingEventInfo chargingEventInfo = null;
    private int assignmentId;

} // ReportNotificationEvent

