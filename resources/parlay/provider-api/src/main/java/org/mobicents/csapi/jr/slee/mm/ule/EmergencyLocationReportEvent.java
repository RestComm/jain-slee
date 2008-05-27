package org.mobicents.csapi.jr.slee.mm.ule;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    Delivery of an emergency user location report.
 * 
 * 
 */
public class EmergencyLocationReportEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for EmergencyLocationReportEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public EmergencyLocationReportEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpUserLocationEmergency location ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.location = location;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the location
     * 
     */
    public org.csapi.mm.TpUserLocationEmergency getLocation() {
        return this.location;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof EmergencyLocationReportEvent)) {
            return false;
        } 
        EmergencyLocationReportEvent emergencyLocationReportEvent = (EmergencyLocationReportEvent) o;
        if(!(this.getService() == emergencyLocationReportEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == emergencyLocationReportEvent.assignmentId)) {
            return false;
        }
        if ((this.location != null) && (emergencyLocationReportEvent.location != null)) {
            if(!(this.location.equals(emergencyLocationReportEvent.location)))  {
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

    private int assignmentId;
    private org.csapi.mm.TpUserLocationEmergency location = null;

} // EmergencyLocationReportEvent

