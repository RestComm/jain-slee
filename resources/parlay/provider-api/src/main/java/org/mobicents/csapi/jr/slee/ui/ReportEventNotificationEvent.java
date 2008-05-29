package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event notifies the application of an occurred network event which matches the criteria installed by the createNotification method.
@return  appUI 
Specifies a reference to the application interface, which implements the callback interface for the new user interaction. 
If the application has previously explicitly passed a reference to the IpAppUI interface using a setCallbackWithSessionID() invocation, this parameter may be null, or if supplied must be the same as that provided during the setCallbackWithSessionID().
 * 
 * 
 */
public class ReportEventNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportEventNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportEventNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpUIIdentifier userInteraction , org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.userInteraction = userInteraction;
        this.eventNotificationInfo = eventNotificationInfo;
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
     * Returns the eventNotificationInfo
     * 
     */
    public org.csapi.ui.TpUIEventNotificationInfo getEventNotificationInfo() {
        return this.eventNotificationInfo;
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
        if(!(o instanceof ReportEventNotificationEvent)) {
            return false;
        } 
        ReportEventNotificationEvent reportEventNotificationEvent = (ReportEventNotificationEvent) o;
        if(!(this.getService() == reportEventNotificationEvent.getService())) {
            return false;
        }
        if ((this.userInteraction != null) && (reportEventNotificationEvent.userInteraction != null)) {
            if(!(this.userInteraction.equals(reportEventNotificationEvent.userInteraction)))  {
                return false;
            }
        }
        if ((this.eventNotificationInfo != null) && (reportEventNotificationEvent.eventNotificationInfo != null)) {
            if(!(this.eventNotificationInfo.equals(reportEventNotificationEvent.eventNotificationInfo)))  {
                return false;
            }
        }
        if(!(this.assignmentID == reportEventNotificationEvent.assignmentID)) {
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
    private org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo = null;
    private int assignmentID;

} // ReportEventNotificationEvent

