package org.mobicents.csapi.jr.slee.mm.us;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    Delivery of a report that is indicating that a user's status has changed.
 * 
 * 
 */
public class TriggeredStatusReportEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredStatusReportEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredStatusReportEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpUserStatus status ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.status = status;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the status
     * 
     */
    public org.csapi.mm.TpUserStatus getStatus() {
        return this.status;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TriggeredStatusReportEvent)) {
            return false;
        } 
        TriggeredStatusReportEvent triggeredStatusReportEvent = (TriggeredStatusReportEvent) o;
        if(!(this.getService() == triggeredStatusReportEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == triggeredStatusReportEvent.assignmentId)) {
            return false;
        }
        if ((this.status != null) && (triggeredStatusReportEvent.status != null)) {
            if(!(this.status.equals(triggeredStatusReportEvent.status)))  {
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
    private org.csapi.mm.TpUserStatus status = null;

} // TriggeredStatusReportEvent

