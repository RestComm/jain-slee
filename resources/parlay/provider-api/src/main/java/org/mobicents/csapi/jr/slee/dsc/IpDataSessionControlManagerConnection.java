package org.mobicents.csapi.jr.slee.dsc;

/**
 * This interface is the 'SCF manager' interface for Data Session Control.  This interface shall be implemented by a Data Session Control SCF.  As a minimum requirement, the createNotifications() and destroyNotification(), or the enableNotifications() and disableNotifications() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpDataSessionControlManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpDataSession interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpDataSessionConnection interface
     */
org.mobicents.csapi.jr.slee.dsc.IpDataSessionConnection  getIpDataSessionConnection(TpDataSessionIdentifier dataSessionIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *  @deprecated    This method is deprecated and will be removed in a later release.  It is replaced with createNotifications().
This method is used to enable data session notifications so that events can be sent to the application. This is the first step an application has to do to get initial notifications of data session happening in the network. When such an event happens, the application will be informed by reportNotification(). In case the application is interested in other events during the context of a particular data session it has to use the connectReq() method on the data session object. The application will get access to the data session object when it receives the reportNotification().
The createNotification method is purely intended for applications to indicate their interest to be notified when certain data session events take place. It is possible to subscribe to a certain event for a whole range of addresses, e.g. the application can indicate it wishes to be informed when a data session is setup to any number starting with 800. 
If some application already requested notifications with criteria that overlap the specified criteria or the specified criteria overlap with criteria already present in the network (when provisioned from within the network), the request is refused with P_INVALID_CRITERIA. The criteria are said to overlap if both originating and terminating ranges overlap and the same number plan is used.
If a notification is requested by an application with monitor mode set to notify, then there is no need to check the rest of the criteria for overlapping with any existing request as the notify mode does not give control of a data session. Only one application can place an interrupt request if the criteria overlaps.
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. In case the createNotification contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
@return assignmentID : Specifies the ID assigned by the Data Session Manager object for this newly-enabled event notification.
     *     @param eventCriteria Specifies the event specific criteria used by the application to define the event required. Individual addresses or address ranges may be specified for destination and/or origination. Examples of events are "Data Session set up".

     */
    int createNotification(org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable data session notifications. This method only applies to notifications created with createNotification().
     *     @param assignmentID Specifies the assignment ID given by the data session manager object when the previous createNotification() was done.

     */
    void destroyNotification(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with the createNotification method. Any stored notification request associated with the specified assignmentID will be replaced with the specified events requested. 
     *     @param assignmentID Specifies the ID assigned by the manager interface for the event notification.
    @param eventCriteria Specifies the new set of event criteria used by the application to define the event required. Only events that meet these criteria are reported.

     */
    void changeNotification(int assignmentID,org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *  @deprecated    This method is deprecated and its use is discouraged.  It will be removed in a later release.  It is replaced with getNotifications.
This method is used by the application to query the event criteria set with createNotification or changeNotification. 
@return eventCriteria : Specifies the event criteria used by the application to define the event required. Only events that meet these requirements are reported.
     * 
     */
    org.csapi.dsc.TpDataSessionEventCriteria getNotification() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is able to receive which are provisioned from within the network (i.e. these notifications are NOT set using createNotification() but via, for instance, a network management system). If notifications provisioned for this application are created or changed, the application is unaware of this until the notification is reported.
If the same application requests to enable notifications for a second time with a different IpAppDataSessionControlManager reference (i.e. without first disabling them), the second callback will be treated as an additional callback. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used.
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
     *     This method replaces getNotification().
This method is used by the application to query the event criteria set with createNotification or changeNotification. 
@return eventCriteria: the list of event criteria  for the notifications requested by the application.   If there is no information to return (e.g. no notifications requested by the application), an empty set (zero length) is returned.
     * 
     */
    org.csapi.dsc.TpDataSessionEventCriteriaResult[] getNotifications() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method is deprecated and will be removed in a later release.  It is replaced with createNotifications().
This method is used to enable data session notifications so that events can be sent to the application. This is the first step an application has to do to get initial notifications of data session happening in the network. When such an event happens, the application will be informed by reportNotification(). In case the application is interested in other events during the context of a particular data session it has to use the connectReq() method on the data session object. The application will get access to the data session object when it receives the reportNotification().
The createNotification method is purely intended for applications to indicate their interest to be notified when certain data session events take place. It is possible to subscribe to a certain event for a whole range of addresses, e.g. the application can indicate it wishes to be informed when a data session is setup to any number starting with 800. 
If some application already requested notifications with criteria that overlap the specified criteria or the specified criteria overlap with criteria already present in the network (when provisioned from within the network), the request is refused with P_INVALID_CRITERIA. The criteria are said to overlap if both originating and terminating ranges overlap and the same number plan is used.
If a notification is requested by an application with monitor mode set to notify, then there is no need to check the rest of the criteria for overlapping with any existing request as the notify mode does not give control of a data session. Only one application can place an interrupt request if the criteria overlaps.
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. In case the createNotification contains no callback, at the moment the application needs to be informed the gateway will use as callback the callback that has been registered by setCallback().
@return assignmentID : Specifies the ID assigned by the Data Session Manager object for this newly-enabled event notification. 
     *     @param eventCriteria Specifies the event specific criteria used by the application to define the event required. Individual addresses or address ranges may be specified for destination and/or origination. Examples of events are "Data Session set up".

     */
    int createNotifications(org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


} // IpDataSessionControlManagerConnection

