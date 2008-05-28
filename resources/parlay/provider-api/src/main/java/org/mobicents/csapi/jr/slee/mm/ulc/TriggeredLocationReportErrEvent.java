package org.mobicents.csapi.jr.slee.mm.ulc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that a requested triggered location report has failed. Note that errors only concerning individual users are reported in the ordinary triggeredLocationReport() message.
 * 
 * 
 */
public class TriggeredLocationReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredLocationReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredLocationReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpMobilityError cause , org.csapi.mm.TpMobilityDiagnostic diagnostic ){
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
        if(!(o instanceof TriggeredLocationReportErrEvent)) {
            return false;
        } 
        TriggeredLocationReportErrEvent triggeredLocationReportErrEvent = (TriggeredLocationReportErrEvent) o;
        if(!(this.getService() == triggeredLocationReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == triggeredLocationReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.cause != null) && (triggeredLocationReportErrEvent.cause != null)) {
            if(!(this.cause.equals(triggeredLocationReportErrEvent.cause)))  {
                return false;
            }
        }
        if ((this.diagnostic != null) && (triggeredLocationReportErrEvent.diagnostic != null)) {
            if(!(this.diagnostic.equals(triggeredLocationReportErrEvent.diagnostic)))  {
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

} // TriggeredLocationReportErrEvent

