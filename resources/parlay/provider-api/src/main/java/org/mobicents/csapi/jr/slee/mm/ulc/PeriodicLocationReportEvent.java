package org.mobicents.csapi.jr.slee.mm.ulc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    Periodic delivery of mobile location reports. The reports are containing mobile-related location information for one or several users.
 * 
 * 
 */
public class PeriodicLocationReportEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for PeriodicLocationReportEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public PeriodicLocationReportEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpUserLocationCamel[] locations ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.locations = locations;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the locations
     * 
     */
    public org.csapi.mm.TpUserLocationCamel[] getLocations() {
        return this.locations;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof PeriodicLocationReportEvent)) {
            return false;
        } 
        PeriodicLocationReportEvent periodicLocationReportEvent = (PeriodicLocationReportEvent) o;
        if(!(this.getService() == periodicLocationReportEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == periodicLocationReportEvent.assignmentId)) {
            return false;
        }
        if ((this.locations != null) && (periodicLocationReportEvent.locations != null)) {
            if(!(this.locations.equals(periodicLocationReportEvent.locations)))  {
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
    private org.csapi.mm.TpUserLocationCamel[] locations = null;

} // PeriodicLocationReportEvent

