package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 * @deprecated    This event is deprecated and replaced by reportEventNotification().  It will be removed in a later release.
This method notifies the application of an occurred network event which matches the criteria installed by the createNotification method.
@return  appUI 
Specifies a reference to the application interface, which implements the callback interface for the new user interaction.
If the application has previously explicitly passed a reference to the IpAppUI interface using a setCallbackWithSessionID() invocation, this parameter may be null, or if supplied must be the same as that provided during the setCallbackWithSessionID().
 * 
 * 
 */
public class ReportNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUIIdentifier userInteraction , org.csapi.ui.TpUIEventInfo eventInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.userInteraction = userInteraction;
        this.eventInfo = eventInfo;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the userInteraction
     * 
     */
    public TpUIIdentifier getUserInteraction() {
        return this.userInteraction;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.ui.TpUIEventInfo getEventInfo() {
        return this.eventInfo;
    }
    /**
     * Returns the assignmentID
     * 
     */
    public int getAssignmentID() {
        return this.assignmentID;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof ReportNotificationEvent)) {
            return false;
        } 
        ReportNotificationEvent reportNotificationEvent = (ReportNotificationEvent) o;
        if(!(this.getService() == reportNotificationEvent.getService())) {
            return false;
        }
        if ((this.userInteraction != null) && (reportNotificationEvent.userInteraction != null)) {
            if(!(this.userInteraction.equals(reportNotificationEvent.userInteraction)))  {
                return false;
            }
        }
        if ((this.eventInfo != null) && (reportNotificationEvent.eventInfo != null)) {
            if(!(this.eventInfo.equals(reportNotificationEvent.eventInfo)))  {
                return false;
            }
        }
        if(!(this.assignmentID == reportNotificationEvent.assignmentID)) {
            return false;
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

    private TpUIIdentifier userInteraction = null;
    private org.csapi.ui.TpUIEventInfo eventInfo = null;
    private int assignmentID;

} // ReportNotificationEvent

