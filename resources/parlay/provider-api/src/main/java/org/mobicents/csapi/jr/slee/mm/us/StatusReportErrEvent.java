package org.mobicents.csapi.jr.slee.mm.us;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the status report request has failed.
 * 
 * 
 */
public class StatusReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for StatusReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public StatusReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpMobilityError cause , org.csapi.mm.TpMobilityDiagnostic diagnostic ){
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
        if(!(o instanceof StatusReportErrEvent)) {
            return false;
        } 
        StatusReportErrEvent statusReportErrEvent = (StatusReportErrEvent) o;
        if(!(this.getService() == statusReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == statusReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.cause != null) && (statusReportErrEvent.cause != null)) {
            if(!(this.cause.equals(statusReportErrEvent.cause)))  {
                return false;
            }
        }
        if ((this.diagnostic != null) && (statusReportErrEvent.diagnostic != null)) {
            if(!(this.diagnostic.equals(statusReportErrEvent.diagnostic)))  {
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

} // StatusReportErrEvent

