package org.mobicents.csapi.jr.slee.mm.ulc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    Delivery of a report that is indicating that the user's mobile location has changed.
 * 
 * 
 */
public class TriggeredLocationReportEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for TriggeredLocationReportEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public TriggeredLocationReportEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int assignmentId , org.csapi.mm.TpUserLocationCamel location , org.csapi.mm.TpLocationTriggerCamel criterion ){
        super(tpServiceIdentifier);
        this.assignmentId = assignmentId;
        this.location = location;
        this.criterion = criterion;
    }

    /**
     * Returns the assignmentId
     * 
     */
    public int getAssignmentId() {
        return this.assignmentId;
    }
    /**
     * Returns the location
     * 
     */
    public org.csapi.mm.TpUserLocationCamel getLocation() {
        return this.location;
    }
    /**
     * Returns the criterion
     * 
     */
    public org.csapi.mm.TpLocationTriggerCamel getCriterion() {
        return this.criterion;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof TriggeredLocationReportEvent)) {
            return false;
        } 
        TriggeredLocationReportEvent triggeredLocationReportEvent = (TriggeredLocationReportEvent) o;
        if(!(this.getService() == triggeredLocationReportEvent.getService())) {
            return false;
        }
        if(!(this.assignmentId == triggeredLocationReportEvent.assignmentId)) {
            return false;
        }
        if ((this.location != null) && (triggeredLocationReportEvent.location != null)) {
            if(!(this.location.equals(triggeredLocationReportEvent.location)))  {
                return false;
            }
        }
        if ((this.criterion != null) && (triggeredLocationReportEvent.criterion != null)) {
            if(!(this.criterion.equals(triggeredLocationReportEvent.criterion)))  {
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
    private org.csapi.mm.TpUserLocationCamel location = null;
    private org.csapi.mm.TpLocationTriggerCamel criterion = null;

} // TriggeredLocationReportEvent

