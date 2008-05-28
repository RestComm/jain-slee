package org.mobicents.csapi.jr.slee.termcap;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This terminal capability report is issued when the capabilities of the terminal have changed in the way specified by the criteria parameter in the previously invoked triggeredTerminalCapabilityStartReq () event. 
 * 
 * 
 */
public class TriggeredTerminalCapabilityReportEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredTerminalCapabilityReportEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredTerminalCapabilityReportEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentID , org.csapi.TpAddress[] terminals , int criteria , org.csapi.termcap.TpTerminalCapabilities capabilities ){
        super(tpServiceIdentifier);
        this.assignmentID = assignmentID;
        this.terminals = terminals;
        this.criteria = criteria;
        this.capabilities = capabilities;
    }

    /**
     * Returns the assignmentID
     * 
     */
    public int getAssignmentID() {
        return this.assignmentID;
    }
    /**
     * Returns the terminals
     * 
     */
    public org.csapi.TpAddress[] getTerminals() {
        return this.terminals;
    }
    /**
     * Returns the criteria
     * 
     */
    public int getCriteria() {
        return this.criteria;
    }
    /**
     * Returns the capabilities
     * 
     */
    public org.csapi.termcap.TpTerminalCapabilities getCapabilities() {
        return this.capabilities;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TriggeredTerminalCapabilityReportEvent)) {
            return false;
        } 
        TriggeredTerminalCapabilityReportEvent triggeredTerminalCapabilityReportEvent = (TriggeredTerminalCapabilityReportEvent) o;
        if(!(this.getService() == triggeredTerminalCapabilityReportEvent.getService())) {
            return false;
        }
        if(!(this.assignmentID == triggeredTerminalCapabilityReportEvent.assignmentID)) {
            return false;
        }
        if ((this.terminals != null) && (triggeredTerminalCapabilityReportEvent.terminals != null)) {
            if(!(this.terminals.equals(triggeredTerminalCapabilityReportEvent.terminals)))  {
                return false;
            }
        }
        if(!(this.criteria == triggeredTerminalCapabilityReportEvent.criteria)) {
            return false;
        }
        if ((this.capabilities != null) && (triggeredTerminalCapabilityReportEvent.capabilities != null)) {
            if(!(this.capabilities.equals(triggeredTerminalCapabilityReportEvent.capabilities)))  {
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

    private int assignmentID;
    private org.csapi.TpAddress[] terminals = null;
    private int criteria;
    private org.csapi.termcap.TpTerminalCapabilities capabilities = null;

} // TriggeredTerminalCapabilityReportEvent

