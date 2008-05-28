package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * The Multi Media Call Control Manager is the factory interface for creating multimedia calls. The multi-media call control manager interface provides the management functions to the multi-media call control service.  The application programmer can use this interface to create, destroy, change and get media stream related notifications.				This interface shall be implemented by a Multi Media Call Control SCF.  As a minimum requirement the createMediaNotification() and destroyMediaNotification() methods shall be implemented. The minimum required methods from IpMultiPartyCallControlManager are also required.
 *
 * 
 * 
 */
public interface IpMultiMediaCallControlManagerConnection extends org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection{


    /**
     *     This method is used to create media stream notifications so that events can be sent to the application.
This applies both to callsetup media (e.g., SIP initial INVITE or H.323 with faststart) and for media setup during the call.
This is the first step an application has to do to get initial notifications of media streams happening in the network. When such an event happens, the application will be informed by reportMediaNotification(). In case the application is interested in other events during the context of a particular call session it has to use the mediaStreamMonitorReq() method on the  Multi-Media call leg object. 
The createMediaNotification method is purely intended for applications to indicate their interest to be notified when certain media stream events take place. It is possible to subscribe to a certain media stream event for a whole range of addresses, e.g. the application can indicate it wishes to be informed when a call is made to any number starting with 800. 
If some application already requested notifications with criteria that overlap the specified criteria, the request is refused with P_INVALID_CRITERIA. The criteria are said to overlap if both originating and terminating ranges overlap and the same number plan is used.
If the same application requests two notifications with exactly the same criteria but different callback references, the second callback will be treated as an additional callback. Both notifications will share the same assignmentID. The gateway will always use the most recent callback. In case this most recent callback fails the second most recent is used. In case the createMediaNotification contains no callback, at the moment the application needs to be informed the gateway will use as callback the one that has been registered by setCallback().
@return assignmentID: Specifies the ID assigned by the multi-media call control manager interface for this newly-created  notification.
     *     @param notificationMediaRequest The mediaMonitorMode is a parameter of TpMediaStreamRequest and can be in interrupt or in notify mode. If in interrupt mode the application has to specify which media streams are allowed by calling mediaStreamAllow on the callLeg.
The notificationMediaRequest parameter specifies the event specific criteria used by the application to define the event required. This is the media portion of the criteria. Only events that meet the notificationMediaRequest are reported.
Individual addresses or address ranges may be specified for the destination and/or origination.

     */
    int createMediaNotification(org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable Multi Media Channel notifications
     *     @param assignmentID Specifies the assignment ID given by the Multi Media call control manager interface when the previous enableMediaNotification was called. If the assignment ID does not correspond to one of the valid assignment IDs, the exception P_INVALID_ASSIGNMENTID will be raised.

     */
    void destroyMediaNotification(int assignmentID) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to change the event criteria introduced with createMediaNotification. Any stored criteria associated with the specified assignmentID will be replaced with the specified criteria. 
     *     @param assignmentID Specifies the ID assigned by the multi-media call control manager interface for the media stream notification. If two callbacks have been registered under this assignment ID both of them will be changed.
    @param notificationMediaRequest Specifies the new set of event specific criteria used by the application to define the event required. Only events that meet these criteria are reported.

     */
    void changeMediaNotification(int assignmentID,org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to query the event criteria set with createMediaNotification or changeMediaNotification.
@return notificationsMediaRequested: Specifies the notifications that have been requested by the application.
     * 
     */
    org.csapi.cc.mmccs.TpMediaNotificationRequested[] getMediaNotification() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiMediaCallControlManagerConnection

