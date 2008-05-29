package org.mobicents.csapi.jr.slee.cc.mpccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event notifies the application of the arrival of a call-related event.
If this method is invoked with a monitor mode of P_CALL_MONITOR_MODE_INTERRUPT, then the APL has control of the call. If the APL does nothing with the call (including its associated legs) within a specified time period (the duration of which forms a part of the service level agreement), then the call in the network shall be released and callEnded() shall be invoked, giving a release cause of P_TIMER_EXPIRY.
Setting the callback reference:
A reference to the application interface has to be passed back to the call interface to which the notification relates.
However, the setting of a call back reference is only applicable if the notification is in INTERRUPT mode.
The call back reference can be registered either a) in reportNotification() or b) explicitly with a setCallbackWithSessionID() method depending on how the application provides its callback reference.
Case a:
From an efficiency point of view the reportNotification() with explicit pass of registration may be the preferred method,  
Case b::
The reportNotification() with no callback reference  ("Null" value) is used where (e.g. due to distributed application logic) the call back reference is provided subsequently in a setCallbackWithSessionID(). 
In case reportNotification() contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallbackWithSessionID().
@return appCallBack: Specifies references to the application interface which implements the callback interface for the new call and/or new call leg.  If the application has previously explicitly passed a reference to the callback interface using a setCallbackWithSessionID() invocation, this parameter may be set to P_APP_CALLBACK_UNDEFINED, or if supplied must be the same as that provided during the setCallbackWithSessionID(). 
This parameter will be set to P_APP_CALLBACK_UNDEFINED if the notification is in NOTIFY mode and in case b).
 * 
 * 
 */
public class ReportNotificationEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for ReportNotificationEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public ReportNotificationEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier callReference , TpCallLegIdentifier[] tpCallLegIdentifier , org.csapi.cc.TpCallNotificationInfo notificationInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.callReference = callReference;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.notificationInfo = notificationInfo;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the callReference
     * 
     */
    public TpMultiPartyCallIdentifier getCallReference() {
        return this.callReference;
    }
    /**
     * Returns the tpCallLegIdentifier
     * 
     */
    public TpCallLegIdentifier[] getTpCallLegIdentifier() {
        return this.tpCallLegIdentifier;
    }
    /**
     * Returns the notificationInfo
     * 
     */
    public org.csapi.cc.TpCallNotificationInfo getNotificationInfo() {
        return this.notificationInfo;
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
        if ((this.callReference != null) && (reportNotificationEvent.callReference != null)) {
            if(!(this.callReference.equals(reportNotificationEvent.callReference)))  {
                return false;
            }
        }
        if ((this.tpCallLegIdentifier != null) && (reportNotificationEvent.tpCallLegIdentifier != null)) {
            if(!(this.tpCallLegIdentifier.equals(reportNotificationEvent.tpCallLegIdentifier)))  {
                return false;
            }
        }
        if ((this.notificationInfo != null) && (reportNotificationEvent.notificationInfo != null)) {
            if(!(this.notificationInfo.equals(reportNotificationEvent.notificationInfo)))  {
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

    private TpMultiPartyCallIdentifier callReference = null;
    private TpCallLegIdentifier[] tpCallLegIdentifier = null;
    private org.csapi.cc.TpCallNotificationInfo notificationInfo = null;
    private int assignmentID;

} // ReportNotificationEvent

