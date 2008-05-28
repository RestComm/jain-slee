package org.mobicents.csapi.jr.slee.termcap;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the requested reporting has failed. Note that errors may concern the whole assignment or just some terminals. In the former case no terminals are specified.
 * 
 * 
 */
public class TriggeredTerminalCapabilityReportErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredTerminalCapabilityReportErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredTerminalCapabilityReportErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.TpAddress[] terminals , org.csapi.termcap.TpTerminalCapabilitiesError cause ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.terminals = terminals;
        this.cause = cause;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the terminals
     * 
     */
    public org.csapi.TpAddress[] getTerminals() {
        return this.terminals;
    }
    /**
     * Returns the cause
     * 
     */
    public org.csapi.termcap.TpTerminalCapabilitiesError getCause() {
        return this.cause;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TriggeredTerminalCapabilityReportErrEvent)) {
            return false;
        } 
        TriggeredTerminalCapabilityReportErrEvent triggeredTerminalCapabilityReportErrEvent = (TriggeredTerminalCapabilityReportErrEvent) o;
        if(!(this.getService() == triggeredTerminalCapabilityReportErrEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == triggeredTerminalCapabilityReportErrEvent.assignmentId)) {
            return false;
        }
        if ((this.terminals != null) && (triggeredTerminalCapabilityReportErrEvent.terminals != null)) {
            if(!(this.terminals.equals(triggeredTerminalCapabilityReportErrEvent.terminals)))  {
                return false;
            }
        }
        if ((this.cause != null) && (triggeredTerminalCapabilityReportErrEvent.cause != null)) {
            if(!(this.cause.equals(triggeredTerminalCapabilityReportErrEvent.cause)))  {
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
    private org.csapi.TpAddress[] terminals = null;
    private org.csapi.termcap.TpTerminalCapabilitiesError cause = null;

} // TriggeredTerminalCapabilityReportErrEvent

