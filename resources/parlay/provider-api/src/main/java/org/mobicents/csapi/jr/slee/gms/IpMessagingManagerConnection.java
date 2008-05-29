package org.mobicents.csapi.jr.slee.gms;

/**
 * This interface is the 'service manager' interface for the Generic Messaging Service. The generic messaging manager interface provides the management functions to the generic messaging service. The application programmer can use this interface to open mailbox objects and also to enable or disable event notifications.
 *
 * 
 * 
 */
public interface IpMessagingManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {

    /**
     * Obtains Access To a IpMailboxConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpMailboxConnection interface
     */
    org.mobicents.csapi.jr.slee.gms.IpMailboxConnection  getIpMailboxConnection(TpMailboxIdentifier mailboxIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method opens a mailbox for the application. The session ID for use by the application is returned. Authentication information may be needed to open the mailbox.
The application can open more than one mailbox at the same time. The application is not allowed to open the same mailbox more than once at the same time.
@return  mailboxReference 
Specifies the reference to the opened mailbox. 
     *     @param mailboxID Specifies the identity of the mailbox. If the mailbox chosen is invalid, the error code P_GMS_INVALID_MAILBOX is returned. 
    @param authenticationInfo Authentication information needed for the application to open a mailbox in the messaging system, such as a key or password. If the authentication process is considered strong enough for the application to gain access to the mailbox, then the authentication information will be an empty string. If the authentication information is not valid, the error code P_GMS_INVALID_AUTHENTICATION_INFORMATION is returned. 

     */
    org.mobicents.csapi.jr.slee.gms.TpMailboxIdentifier openMailbox(org.csapi.TpAddress mailboxID,String authenticationInfo) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,javax.slee.resource.ResourceException;


    /**
     *     This method is used to enable messaging notifications so that events can be sent to the application. 
@return  assignmentID 
Specifies the ID assigned by the generic messaging manager interface for this newly-enabled event notification. 
     *     @param eventCriteria Specifies the event specific criteria used by the application to define the event required.  

     */
    int enableMessagingNotification(org.csapi.gms.TpMessagingEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method is used by the application to disable call notifications.  
     *     @param assignmentID Specifies the assignment ID given by the generic messaging manager interface when the previous enableNotification() was called. If the assignment ID does not correspond to one of the valid assignment IDs, the framework will return the error code P_INVALID_ASSIGNMENTID. 

     */
    void disableMessagingNotification(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpMessagingManagerConnection

