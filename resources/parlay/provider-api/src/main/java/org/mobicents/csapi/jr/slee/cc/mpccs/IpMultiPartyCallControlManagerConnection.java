package org.mobicents.csapi.jr.slee.cc.mpccs;

/**
 * This interface is the 'service manager' interface for the Multi-party Call Control Service.  The multi-party call control manager interface provides the management functions to the multi-party call control service. The application programmer can use this interface to provide overload control functionality, create call objects and to enable or disable call-related event notifications.  The action table associated with the STD shows in what state the IpMultiPartyCallControlManager must be if a method can successfully complete.  In other words, if the IpMultiPartyCallControlManager is in another state the method will throw an exception immediately.						This interface shall be implemented by a Multi Party Call Control SCF.  As a minimum requirement either the createCall() method shall be implemented, or the createNotification() and destroyNotification() methods shall be implemented, or the enableNotifications() and disableNotifications() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpMultiPartyCallControlManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpMultiPartyCallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpMultiPartyCallConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection  getIpMultiPartyCallConnection(TpMultiPartyCallIdentifier multiPartyCallIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new  call object. An IpAppMultiPartyCallControlManager should already have been passed to the IpMultiPartyCallControlManager, otherwise the call control will not be able to report a callAborted() to the application.  The application should invoke setCallback() prior to createCall() if it wishes to ensure this.
@return callReference: Specifies the interface reference and sessionID of the call created.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier createCall() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to enable call notifications so that events can be sent to the application. This is the first step an application has to do to get initial notifications of calls happening in the network. When such an event happens, the application will be informed by reportNotification(). In case the application is interested in other events during the context of a particular call session it has to use the createAndRouteCallLegReq() method on the call object or the eventReportReq() method on the call leg object. The application will get access to the call object when it receives the reportNotification(). (Note that createNotification() is not applicable if the call is setup by the application).
The createNotification method is purely intended for applications to indicate their interest to be notified when certain call events take place. It is possible to subscribe to a certain event for a whole range of addresses, e.g, the application can indicate it wishes to be informed when a call is made to any number starting with 800. 
If some application already requested notifications with criteria that overlap the specified criteria or the specified criteria overlap with criteria already present in the network (when provisioned from within the network), the request is refused with P_INVALID_CRITERIA. The criteria are said to overlap when it leads to more than one application controlling the call or session at the same point in time during call or session processing.
If a notification is requested by an application with monitor mode set to notify, then there is no need to check the rest of the criteria for overlapping with any existing request as the notify mode does not allow control on a call to be passed over. Only one application can place an interrupt request if the criteria overlaps.
Setting the callback reference:
The callback reference can be registered either a) in createNotication() or b) explicitly with a setCallback() method e.g, depending on how the application provides its callback reference.
Case a:
From an efficiency point of view the createNotification() with explicit registration may be the preferred method.
Case b:
The createNotification() with no callback reference ("Null" value) is used where (e.g, due to distributed application logic) the callback reference is provided subsequently in a setcallback(). 
In case the createNotification() contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
Set additional callback:
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. In case the createNotification contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
@return assignmentID: Specifies the ID assigned by the call control manager interface for this newly-enabled event notification.
     *     @param notificationRequest Specifies the event specific criteria used by the application to define the event required. Only events that meet these criteria are reported. Examples of events are "incoming call attempt reported by network", "answer", "no answer", "busy". Individual addresses or address ranges may be specified for destination and/or origination. 

     */
    int createNotification(org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable call notifications. This method only applies to notifications created with createNotification().
     *     @param assignmentID Specifies the assignment ID given by the multi party call control manager interface when the previous createNotification() was called. If the assignment ID does not correspond to one of the valid assignment IDs, the exception P_INVALID_ASSIGNMENTID will be raised. If two callbacks have been registered under this assignment ID both of them will be disabled.

     */
    void destroyNotification(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with createNotification. Any stored criteria associated with the specified assignmentID will be replaced with the specified criteria.
     *     @param assignmentID Specifies the ID assigned by the multi party call control manager interface for the event notification. If two callbacks have been registered under this assignment ID both of them will be changed.
    @param notificationRequest Specifies the new set of event specific criteria used by the application to define the event required. Only events that meet these criteria are reported.

     */
    void changeNotification(int assignmentID,org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *  @deprecated    This method is deprecated and replaced by getNextNotification().  It will be removed in a later release.
This method is used by the application to query the event criteria set with createNotification or changeNotification.
@return notificationsRequested: Specifies the notifications that have been requested by the application.  An empty set is returned when no notifications exist.
     * 
     */
    org.csapi.cc.TpNotificationRequested[] getNotification() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method imposes or removes load control on calls made to a particular address range within the call control service. The address matching mechanism is similar as defined for TpCallEventCriteria.
@return assignmentID: Specifies the assignmentID assigned by the gateway to this request. This assignmentID can be used to correlate the callOverloadEncountered and callOverloadCeased methods with the request.
     *     @param duration Specifies the duration for which the load control should be set.
A duration of 0 indicates that the load control should be removed.
A duration of -1 indicates an infinite duration (i.e., until disabled by the application)
A duration of -2 indicates the network default duration.
    @param mechanism Specifies the load control mechanism to use (for example, admit one call per interval), and any necessary parameters, such as the call admission rate. The contents of this parameter are ignored if the load control duration is set to zero.
    @param treatment Specifies the treatment of calls that are not admitted. The contents of this parameter are ignored if the load control duration is set to zero.
    @param addressRange Specifies the address or address range to which the overload control should be applied or removed.

     */
    int setCallLoadControl(int duration,org.csapi.cc.TpCallLoadControlMechanism mechanism,org.csapi.cc.TpCallTreatment treatment,org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is able to receive notifications which are provisioned from within the network (i.e. these notifications are NOT set using createNotification() but via, for instance, a network management system). If notifications provisioned for this application are created or changed, the application is unaware of this until the notification is reported.
Setting the callback reference:
The callback reference can be registered either a) in enableNotications() or b) explicitly with a setCallback() method e.g, depending on how the application provides its callback reference.
Case a:
From an efficiency point of view the createNotification() with explicit registation  may be the preferred method.
Case b::
The enableNotifications() with no callback reference ("Null" value) is used where (e.g, due to distributed application logic) the callback reference is provided subsequently in a setCallback().
In case the createNotification() contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
Set additional Call back:
If the same application requests to enable notifications for a second time with a different IpAppMultiPartyCallControlManager reference (i.e. without first disabling them), the second callback will be treated as an additional callback. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used.
When this method is used, it is still possible to use createNotification() for service provider provisioned notifications on the same interface as long as the criteria in the network and provided by createNotification() do not overlap. However, it is NOT recommended to use both mechanisms on the same service manager.
The methods changeNotification(), getNotification(), and destroyNotification() do not apply to notifications provisioned in the network and enabled using enableNotifications(). These only apply to notifications created using createNotification().
@return assignmentID: Specifies the ID assigned by the manager interface for this operation. This ID is contained in any reportNotification() that relates to notifications provisioned from within the network.  Repeated calls to enableNotifications() return the same assignment ID.

     * 
     */
    int enableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is not able to receive notifications for which the provisioning has been done from within the network. (i.e. these notifications that are NOT set using createNotification() but via, for instance, a network management system). After this method is called, no such notifications are reported anymore.
     * 
     */
    void disableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the event criteria set with createNotification or changeNotification. Since a lot of data can potentially be returned (which might cause problem in the middleware), this method must be used in an iterative way. Each method invocation  may return part of the total set of notifications if the set is too large to return it at once. The reset parameter permits the application to indicate whether an invocation to getNextNotification is requesting more notifications from the total set of notifications or is requesting that the total set of notifications shall be returned from the beginning.
@return notificationRequestedSetEntry: The set of notifications and an indication whether all off the notifications have been obtained or if more notifications are available that have not yet been obtained by  the application. If no notifications exist, an empty set is returned and the final indication shall be set to TRUE.
Note that the (maximum) number of items provided to the application is determined by the gateway.
     *     @param reset TRUE: indicates that the application is intended to obtain the set of notifications starting at the beginning.
FALSE: indicates that the application requests the next set of notifications that have not (yet) been obtained since the last call to this method with this parameter set to TRUE.
The first time this method is invoked, reset shall be set to TRUE. Following the receipt of a final indication in TpNotificationRequestedSetEntry, for the next call to this method reset shall be set to TRUE. P_TASK_REFUSED may be thrown if these conditions are not met.

     */
    org.csapi.cc.TpNotificationRequestedSetEntry getNextNotification(boolean reset) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiPartyCallControlManagerConnection

