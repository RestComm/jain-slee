package org.mobicents.csapi.jr.slee.mm.ul;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the location report request has failed.
 * 
 * 
 */
public class LocationReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for LocationReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public LocationReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpMobilityError cause , org.csapi.mm.TpMobilityDiagnostic diagnostic ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.cause = cause;
        this.diagnostic = diagnostic;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the cause
     * 
     */
    public org.csapi.mm.TpMobilityError getCause() {
        return this.cause;
    }
    /**
     * Returns the diagnostic
     * 
     */
    public org.csapi.mm.TpMobilityDiagnostic getDiagnostic() {
        return this.diagnostic;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof LocationReportErrEvent)) {
            return false;
        } 
        LocationReportErrEvent locationReportErrEvent = (LocationReportErrEvent) o;
        if(!(this.getService() == locationReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == locationReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.cause != null) && (locationReportErrEvent.cause != null)) {
            if(!(this.cause.equals(locationReportErrEvent.cause)))  {
                return false;
            }
        }
        if ((this.diagnostic != null) && (locationReportErrEvent.diagnostic != null)) {
            if(!(this.diagnostic.equals(locationReportErrEvent.diagnostic)))  {
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
    private org.csapi.mm.TpMobilityError cause = null;
    private org.csapi.mm.TpMobilityDiagnostic diagnostic = null;

} // LocationReportErrEvent

