package org.mobicents.csapi.jr.slee.mm.ul;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    A report containing extended location information for one or several users is delivered.
 * 
 * 
 */
public class ExtendedLocationReportResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ExtendedLocationReportResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ExtendedLocationReportResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpUserLocationExtended[] locations ){
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
    public org.csapi.mm.TpUserLocationExtended[] getLocations() {
        return this.locations;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ExtendedLocationReportResEvent)) {
            return false;
        } 
        ExtendedLocationReportResEvent extendedLocationReportResEvent = (ExtendedLocationReportResEvent) o;
        if(!(this.getService() == extendedLocationReportResEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == extendedLocationReportResEvent.assignmentId)) {
            return false;
        }
        if ((this.locations != null) && (extendedLocationReportResEvent.locations != null)) {
            if(!(this.locations.equals(extendedLocationReportResEvent.locations)))  {
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
    private org.csapi.mm.TpUserLocationExtended[] locations = null;

} // ExtendedLocationReportResEvent

