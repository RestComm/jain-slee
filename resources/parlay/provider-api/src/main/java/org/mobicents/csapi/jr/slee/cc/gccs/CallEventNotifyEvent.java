package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event notifies the application of the arrival of a call-related event. 
If this method is invoked with a monitor mode of P_CALL_MONITOR_MODE_INTERRUPT, then the APL has control of the call. If the APL does nothing with the call (including its associated legs) within a specified time period (the duration of which forms a part of the service level agreement), then the call in the network shall be released and callEnded() shall be invoked, giving a release cause of 102 (Recovery on timer expiry).
Setting the callback reference:
A reference to the application interface has to be passed back to the call interface to which the notification relates.
However, the setting of a call back reference is only applicable if the notification is in INTERRUPT mode. 
When the callEventNotify() method is invoked with a monitor mode of P_CALL_MONITOR_MODE_INTERRUPT, the application writer should ensure that no continue processing e.g. routeReq() is performed until an IpAppCall has been passed to the gateway, either through an explicit setCallbackWithSessionID() invocation on the supplied IpCall, or via the return of the callEventNotify() method.
The callback reference can be registered either in a) callEventNotify() or b) explicitly with a setCallbackWithSessionID() method e.g. depending on how the application provides its call reference.
Case a:
From an efficiency point of view the callEventNotify() with explicit pass of registration may be the preferred method.
Case b::
The callEventNotify with no callback reference ("Null" value) is used where (e.g. due to distributed application logic) the callback reference is provided subsequently in a setCallbackWithSessionID().
In case the callEventNotify() contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallbackWithSessionID().  See example in 4.6.
@return appCall: Specifies a reference to the application interface which implements the callback interface for the new call. If the application has previously explicitly passed a reference to the IpAppCall interface using a setCallbackWithSessionID() invocation, this parameter may be null, or if supplied must be the same as that provided during the setCallbackWithSessionID(). 
This parameterwill be null if the notification is in NOTIFY mode and in case b).
 * 
 * 
 */
public class CallEventNotifyEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for CallEventNotifyEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public CallEventNotifyEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier callReference , org.csapi.cc.gccs.TpCallEventInfo eventInfo , int assignmentID ){
        super(tpServiceIdentifier);
        this.callReference = callReference;
        this.eventInfo = eventInfo;
        this.assignmentID = assignmentID;
    }

    /**
     * Returns the callReference
     * 
     */
    public TpCallIdentifier getCallReference() {
        return this.callReference;
    }
    /**
     * Returns the eventInfo
     * 
     */
    public org.csapi.cc.gccs.TpCallEventInfo getEventInfo() {
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
        if(!(o instanceof CallEventNotifyEvent)) {
            return false;
        } 
        CallEventNotifyEvent callEventNotifyEvent = (CallEventNotifyEvent) o;
        if(!(this.getService() == callEventNotifyEvent.getService())) {
            return false;
        }
        if ((this.callReference != null) && (callEventNotifyEvent.callReference != null)) {
            if(!(this.callReference.equals(callEventNotifyEvent.callReference)))  {
                return false;
            }
        }
        if ((this.eventInfo != null) && (callEventNotifyEvent.eventInfo != null)) {
            if(!(this.eventInfo.equals(callEventNotifyEvent.eventInfo)))  {
                return false;
            }
        }
        if(!(this.assignmentID == callEventNotifyEvent.assignmentID)) {
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

    private TpCallIdentifier callReference = null;
    private org.csapi.cc.gccs.TpCallEventInfo eventInfo = null;
    private int assignmentID;

} // CallEventNotifyEvent

