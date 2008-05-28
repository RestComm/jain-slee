package org.mobicents.csapi.jr.slee.ui;

/**
 * This interface is the 'service manager' interface for the Generic User Interaction Service and provides the management functions to the Generic User Interaction Service. 																			This interface shall be implemented by a Generic User Interaction SCF.  The createUI() method, or the createUICall() method, or both the createNotification() and destroyNotification methods, or both the enableNotifications() and disableNotifications() methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpUIManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpUIConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpUIConnection interface
     */
    org.mobicents.csapi.jr.slee.ui.IpUIConnection  getIpUIConnection(TpUIIdentifier uIIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new user interaction object for non-call related purposes 
Results: userInteraction 
Specifies the interface and sessionID of the user interaction created.
     *     @param userAddress Indicates the end-user with whom to interact. 

     */
    org.mobicents.csapi.jr.slee.ui.TpUIIdentifier createUI(org.csapi.TpAddress userAddress) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;

    /**
     * Obtains Access To a IpUICallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpUICallConnection interface
     */
    org.mobicents.csapi.jr.slee.ui.IpUICallConnection  getIpUICallConnection(TpUICallIdentifier uICallIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new user interaction object for call related purposes. 
The user interaction can take place to the specified party or to all parties in a call. Note that for certain implementation user interaction can only be performed towards the controlling call party, which shall be the only party in the call.
@return  userInteraction 
Specifies the interface and sessionID of the user interaction created. 
     *     @param uiTargetObject Specifies the object on which to perform the user interaction. This can either be a Call, Multi-party Call or call leg object.

     */
    org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier createUICall(org.mobicents.csapi.jr.slee.ui.TpUITargetObject uiTargetObject) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to install specified notification criteria, for which the reporting is implicitly activated. If some application already requested notifications with criteria that overlap the specified criteria, or the specified criteria overlap with criteria already present in the network (when provisioned from within the network), the request is refused with P_INVALID_CRITERIA.
The criteria are said to overlap if both originating and terminating ranges overlap and the same number plan is used and the same servicecode is used.
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used.
@return  assignmentID 
Specifies the ID assigned by the generic user interaction manager interface for this newly installed notification criteria.
     *     @param eventCriteria Specifies the event specific criteria used by the application to define the event required, like user address and service code.

     */
    int createNotification(org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to destroy previously installed notification criteria via the createNotification method.
     *     @param assignmentID Specifies the assignment ID given by the generic user interaction manager interface when the previous createNotification() was called. If the assignment ID does not correspond to one of the valid assignment IDs, the framework will return the error code P_INVALID_ASSIGNMENT_ID. 

     */
    void destroyNotification(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with createNotification method. Any stored notification request associated with the specified assignmentID will be replaced with the specified events requested.
     *     @param assignmentID Specifies the ID assigned by the manager interface for the event notification.
    @param eventCriteria Specifies the new set of event criteria used by the application to define the event required. Only events that meet these criteria are reported. 

     */
    void changeNotification(int assignmentID,org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the event criteria set with createNotification or changeNotification.
@return  eventCriteria 
Specifies the event specific criteria used by the application to define the event required. Only events that meet these criteria are reported.
     * 
     */
    org.csapi.ui.TpUIEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is able to receive notifications which are provisioned from within the network (i.e. these notifications are NOT set using createNotification() but via, for instance, a network management system). If notifications provisioned for this application are created or changed, the application is unaware of this until the notification is reported.
If the same application requests to enable notifications for a second time with a different IpAppUIManager reference (i.e. without first disabling them), the second callback will be treated as an additional callback. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used.
When this method is used, it is still possible to use createNotification() for service provider provisioned notifications on the same interface as long as the criteria in the network and provided by createNotification() do not overlap. However, it is NOT recommended to use both mechanisms on the same service manager.
The methods changeNotification(), getNotification(), and destroyNotification() do not apply to notifications provisioned in the network and enabled using enableNotifications(). These only apply to notifications created using createNotification().
@return assignmentID: Specifies the ID assigned by the manager interface for this operation. This ID is contained in any reportNotification() that relates to notifications provisioned from within the network.
     * 
     */
    int enableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate that the application is not able to receive notifications for which the provisioning has been done from within the network. (i.e. these notifications that are NOT set using createNotification() but via, for instance, a network management system). After this method is called, no such notifications are reported anymore.
     * 
     */
    void disableNotifications() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpUIManagerConnection

