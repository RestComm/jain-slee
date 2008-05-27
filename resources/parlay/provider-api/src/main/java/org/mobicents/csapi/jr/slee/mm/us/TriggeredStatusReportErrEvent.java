package org.mobicents.csapi.jr.slee.mm.us;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that a requested triggered status reporting has failed. Note that errors only concerning individual users are reported in the ordinary triggeredStatusReport() message.
 * 
 * 
 */
public class TriggeredStatusReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredStatusReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredStatusReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpMobilityError cause , org.csapi.mm.TpMobilityDiagnostic diagnostic ){
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
        if(!(o instanceof TriggeredStatusReportErrEvent)) {
            return false;
        } 
        TriggeredStatusReportErrEvent triggeredStatusReportErrEvent = (TriggeredStatusReportErrEvent) o;
        if(!(this.getService() == triggeredStatusReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == triggeredStatusReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.cause != null) && (triggeredStatusReportErrEvent.cause != null)) {
            if(!(this.cause.equals(triggeredStatusReportErrEvent.cause)))  {
                return false;
            }
        }
        if ((this.diagnostic != null) && (triggeredStatusReportErrEvent.diagnostic != null)) {
            if(!(this.diagnostic.equals(triggeredStatusReportErrEvent.diagnostic)))  {
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

} // TriggeredStatusReportErrEvent

