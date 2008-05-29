package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event notifies the application of the arrival of a data session-related event.
If this method is invoked with a monitor mode of P_DATA_SESSION_MONITOR_MODE_INTERRUPT, then the application has control of the data session. If the application does nothing with the data session within a specified time period (the duration of which forms a part of the service level agreement), then the data session in the network shall be released and dataSessionFaultDetected() shall be invoked, giving a fault code of P_DATA_SESSION_TIMEOUT_ON_INTERRUPT.
@return appDataSession : Specifies a reference to the application object which implements the callback interface for the new data session. If the application has previously explicitly passed a reference to the IpAppDataSession interface using a setCallbackWithSessionID() invocation, this parameter may be null, or if supplied must be the same as that provided during the setCallbackWithSessionID(). 
This parameter will be null if the notification is in NOTIFY mode.
 * 
 * 
 */
public class ReportNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier dataSessionReference , org.csapi.dsc.TpDataSessionEventInfo eventInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.dataSessionReference = dataSessionReference;
        this.eventInfo = eventInfo;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the dataSessionReference
     * 
     */
    public TpDataSessionIdentifier getDataSessionReference() {
        return this.dataSessionReference;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.dsc.TpDataSessionEventInfo getEventInfo() {
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
        if ((this.dataSessionReference != null) && (reportNotificationEvent.dataSessionReference != null)) {
            if(!(this.dataSessionReference.equals(reportNotificationEvent.dataSessionReference)))  {
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

    private TpDataSessionIdentifier dataSessionReference = null;
    private org.csapi.dsc.TpDataSessionEventInfo eventInfo = null;
    private int assignmentID;

} // ReportNotificationEvent

