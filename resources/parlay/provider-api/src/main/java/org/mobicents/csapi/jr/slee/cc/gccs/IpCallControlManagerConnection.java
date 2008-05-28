package org.mobicents.csapi.jr.slee.cc.gccs;

/**
 * This interface is the 'service manager' interface for the Generic Call Control Service.  The generic call control manager interface provides the management functions to the generic call control service. The application programmer can use this interface to provide overload control functionality, create call objects and to enable or disable call-related event notifications.																														This interface shall be implemented by a Generic Call Control SCF.  As a minimum requirement either the createCall() method shall be implemented, or the enableCallNotification() and disableCallNotification() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpCallControlManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpCallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpCallConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection  getIpCallConnection(TpCallIdentifier callIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new  call object. 
Call back reference:
An IpAppCallControlManager should already have been passed to the IpCallControlManager, otherwise the call control will not be able to report a callAborted() to the application. The application should invoke setCallback() prior to createCall() if it wishes to ensure this.
@return callReference: Specifies the interface reference and sessionID of the call created.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier createCall() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to enable call notifications so that events can be sent to the application. This is the first step an application has to do to get initial notification of calls happening in the network. When such an event happens, the application will be informed by callEventNotify(). In case the application is interested in other events during the context of a particular call session it has to use the routeReq() method on the call object. The application will get access to the call object when it receives the callEventNotify(). (Note that the enableCallNotification() is not applicable if the call is setup by the application). 
The enableCallNotification method is purely intended for applications to indicate their interest to be notified when certain call events take place. It is possible to subscribe to a certain event for a whole range of addresses, e.g. the application can indicate it wishes to be informed when a call is made to any number starting with 800.
If some application already requested notifications with criteria that overlap the specified criteria, the request is refused with P_GCCS_INVALID_CRITERIA. The criteria are said to overlap if both originating and terminating ranges overlap and the same number plan is used and the same CallNotificationType is used.
If a notification is requested by an application with the monitor mode set to notify, then there is no need to check the rest of the criteria for overlapping with any existing request as the notify mode does not allow control on a call to be passed over.  Only one application can place an interrupt request if the criteria overlaps.
Setting the callback reference:
The callback reference can be registered either a) in enableCallNotification() or b) explicitly with a separate setCallback() method  depending on how the application provides its callback reference.
Case a:
From an efficiency point of view the enableCallNotification() with explicit immediate registration (no "Null" value)  of callback reference may be the preferred method.
Case b::
The enableCallNotfication() with no callback reference ("Null" value) is used where (e.g. due to distributed application logic) the call back reference is provided subsequently in a setCallback(). 
In case the enableCallNotification() contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback(). See example in 4.6
Set additional callback:
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. See example in 4.1.
@return assignmentID: Specifies the ID assigned by the generic call control manager interface for this newly-enabled event notification.
     *     @param eventCriteria Specifies the event specific criteria used by the application to define the event required. Only events that meet these criteria are reported. Examples of events are "incoming call attempt reported by network", "answer", "no answer", "busy". Individual addresses or address ranges may be specified for destination and/or origination. 

     */
    int enableCallNotification(org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable call notifications. 
     *     @param assignmentID Specifies the assignment ID given by the generic call control manager interface when the previous enableCallNotification() was called. If the assignment ID does not correspond to one of the valid assignment IDs, the exception P_INVALID_ASSIGNMENTID will be raised. If two callbacks have been registered under this assignment ID both of them will be disabled.

     */
    void disableCallNotification(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     This method imposes or removes load control on calls made to a particular address range within the generic call control service. The address matching mechanism is similar as defined for TpCallEventCriteria.
@return assignmentID: Specifies the assignmentID assigned by the gateway to this request. This assignmentID can be used to correlate the callOverloadEncountered and callOverloadCeased methods with the request.
     *     @param duration Specifies the duration for which the load control should be set.
A duration of 0 indicates that the load control should be removed.
A duration of -1 indicates an infinite duration (i.e., until disabled by the application)
A duration of -2 indicates the network default duration.
    @param mechanism Specifies the load control mechanism to use (for example, admit one call per interval), and any necessary parameters, such as the call admission rate. The contents of this parameter are ignored if the load control duration is set to zero.
    @param treatment Specifies the treatment of calls that are not admitted. The contents of this parameter are ignored if the load control duration is set to zero.
    @param addressRange Specifies the address or address range to which the overload control should be applied or removed.

     */
    int setCallLoadControl(int duration,org.csapi.cc.TpCallLoadControlMechanism mechanism,org.csapi.cc.gccs.TpCallTreatment treatment,org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with enableCallNotification. Any stored criteria associated with the specified assignmentID will be replaced with the specified criteria.
     *     @param assignmentID Specifies the ID assigned by the generic call control manager interface for the event notification. If two call backs have been registered under this assignment ID both of them will be changed.
    @param eventCriteria Specifies the new set of event specific criteria used by the application to define the event required. Only events that meet these criteria are reported.

     */
    void changeCallNotification(int assignmentID,org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the event criteria set with enableCallNotification or changeCallNotification.
@return eventCriteria: Specifies the event specific criteria used by the application to define the event required. Only events that meet these criteria are reported.
     * 
     */
    org.csapi.cc.gccs.TpCallEventCriteriaResult[] getCriteria() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpCallControlManagerConnection

