package org.mobicents.csapi.jr.slee.mm.ul;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that a requested periodic location report has failed. Note that errors only concerning individual users are reported in the ordinary periodicLocationReport() message.
 * 
 * 
 */
public class PeriodicLocationReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for PeriodicLocationReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public PeriodicLocationReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpMobilityError cause , org.csapi.mm.TpMobilityDiagnostic diagnostic ){
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
        if(!(o instanceof PeriodicLocationReportErrEvent)) {
            return false;
        } 
        PeriodicLocationReportErrEvent periodicLocationReportErrEvent = (PeriodicLocationReportErrEvent) o;
        if(!(this.getService() == periodicLocationReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == periodicLocationReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.cause != null) && (periodicLocationReportErrEvent.cause != null)) {
            if(!(this.cause.equals(periodicLocationReportErrEvent.cause)))  {
                return false;
            }
        }
        if ((this.diagnostic != null) && (periodicLocationReportErrEvent.diagnostic != null)) {
            if(!(this.diagnostic.equals(periodicLocationReportErrEvent.diagnostic)))  {
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

} // PeriodicLocationReportErrEvent

