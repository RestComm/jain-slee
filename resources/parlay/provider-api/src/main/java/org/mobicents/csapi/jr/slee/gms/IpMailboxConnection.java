package org.mobicents.csapi.jr.slee.gms;

/**
 * This class represents the Parlay < IpMailboxConnection > interface
 *
 * 
 * 
 */
public interface IpMailboxConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method closes the mailbox. After closing, the interfaces to the mailbox and any associated folders are automatically de-assigned and are no longer valid. Any open folders will also be automatically closed. 
     * 
     */
    void close() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method locks the mailbox so that only the requesting application can have access to this mailbox. Updates to the mailbox by other applications or the network are not permitted until the mailbox has been unlocked - attempts to do so result in the error code P_GMS_MAILBOX_LOCKED. When the application exits, however, all mailboxes locked by the application are unlocked automatically.
The service returns an error code P_GMS_LOCKING_LOCKED_MAILBOX when the application attempts to lock a mailbox that is locked.
     * 
     */
    void lock() throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX,javax.slee.resource.ResourceException;


    /**
     *     This method unlocks a previously locked mailbox. An error is returned if the mailbox is already unlocked. 
     * 
     */
    void unlock() throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOX,org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX,javax.slee.resource.ResourceException;


    /**
     *     This method returns the number of mailbox information properties of the specified mailbox. 
@return  numberOfProperties 
The number of properties associated with the folder. The number of properties is zero or positive. 
     * 
     */
    int getInfoAmount() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method returns the properties of a mailbox. 
@return  mailboxInfoProperties 
The mailbox information properties (names and values) present in the folder.
     *     @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be retrieved from the mailbox is located. Properties are numbered from zero. 
    @param numberOfProperties The number of properties to return. If the value of this parameter is zero, then all properties will be returned. Otherwise, the value must be a positive number. If the number is not positive, the error code P_GMS_NUMBER_NOT_POSITIVE is returned. 

     */
    org.csapi.gms.TpMailboxInfoProperty[] getInfoProperties(int firstProperty,int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE,javax.slee.resource.ResourceException;


    /**
     *     Sets the properties of a mailbox. 
     *     @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be updated in the mailbox is located. Properties are numbered from zero. 
    @param mailboxInfoProperties This specifies the mailbox information properties (names and values) to be set in the mailbox. If the properties cannot be changed, then the error code P_GMS_PROPERTY_NOT_SET is returned.  

     */
    void setInfoProperties(int firstProperty,org.csapi.gms.TpMailboxInfoProperty[] mailboxInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.gms.P_GMS_MAILBOX_LOCKED,javax.slee.resource.ResourceException;

    /**
     * Obtains Access To a IpMailboxFolderConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpMailboxFolderConnection interface
     */
    org.mobicents.csapi.jr.slee.gms.IpMailboxFolderConnection  getIpMailboxFolderConnection(TpMailboxFolderIdentifier mailboxFolderIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method opens a folder for the application, and returns a folder session ID and a reference to the interface of the folder opened.
The application can open more than one folder at the same time. The application is not allowed to open the same folder more than once at the same time. If the folder is already open, the error code P_GMS_FOLDER_IS_OPEN is returned.
@return  folderReference 
Specifies the reference to the opened folder. 
     *     @param folderID Specifies the identity of the folder. If the folder ID given is not present, the error code P_GMS_INVALID_FOLDER_ID is returned. 

     */
    org.mobicents.csapi.jr.slee.gms.TpMailboxFolderIdentifier openFolder(String folderID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED,javax.slee.resource.ResourceException;


    /**
     *     This method creates a new folder in the mailbox. 
     *     @param folderID Specifies the identity of the folder. If the folder ID given is already present, the error code P_GMS_INVALID_FOLDER_ID is returned. 

     */
    void createFolder(String folderID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED,javax.slee.resource.ResourceException;


    /**
     *     This method removes a mailbox from the messaging system for the application. Authentication information may be needed to remove the mailbox. If the application does not have sufficient privilege to remove the mailbox, the error code P_GMS_INSUFFICIENT_PRIVILEGE is returned. 
     *     @param mailboxID Specifies the identity of the mailbox. If the mailbox chosen is invalid, the error code P_GMS_INVALID_MAILBOX is returned. If the mailbox is locked then the error code P_GMS_MAILBOX_LOCKED is returned. If the mailbox is open then the error code P_GMS_MAILBOX_OPEN is returned. 
    @param authenticationInfo Authentication information needed for the application to remove a mailbox from the messaging system, such as a key or password. If the authentication process is considered strong enough for the application to gain access to the mailbox, then the authentication information will be an empty string. If the authentication information is not valid, the error code P_GMS_INVALID_AUTHENTICATION_INFORMATION is returned. 

     */
    void remove(org.csapi.TpAddress mailboxID,String authenticationInfo) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.gms.P_GMS_MAILBOX_LOCKED,org.csapi.gms.P_GMS_MAILBOX_OPEN,org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,javax.slee.resource.ResourceException;


} // IpMailboxConnection

