package org.mobicents.csapi.jr.slee.gms;

/**
 * This class represents the Parlay < IpMailboxFolderConnection > interface
 *
 * 
 * 
 */
public interface IpMailboxFolderConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method returns the number of folder information properties of the specified folder. 
@return  numberOfProperties 
The number of properties associated with the folder. The number of properties is zero or positive.
     * 
     */
    int getInfoAmount() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method returns the properties of a folder. 
@return  folderInfoProperties 
The folder information properties (names and values) present in the folder. Folder properties include parent folder, sub folders, number of messages contained, date created, date last accessed, and read/write access. 
     *     @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be retrieved from the folder is located. Properties are numbered from zero. 
    @param numberOfProperties The number of properties to return. If the value of this parameter is zero, then all properties will be returned. Otherwise, the value must be a positive number. If the number is not positive, the error code P_GMS_NUMBER_NOT_POSITIVE is returned. 

     */
    org.csapi.gms.TpFolderInfoProperty[] getInfoProperties(int firstProperty,int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE,javax.slee.resource.ResourceException;


    /**
     *     Sets the properties of a folder. 
     *     @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be updated in the folder is located. Properties are numbered from zero. 
    @param folderInfoProperties This specifies the folder information properties (names and values) to be set in the folder. Folder properties that may be changed include parent folder, sub folders and read/write access. If the properties cannot be changed, then the error code P_GMS_PROPERTY_NOT_SET is returned.  

     */
    void setInfoProperties(int firstProperty,org.csapi.gms.TpFolderInfoProperty[] folderInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,javax.slee.resource.ResourceException;


    /**
     *     This method puts a message into an open mailbox folder. The message and the headers are transferred to the Messaging service. The message will be taken as is. No checking is done on the message. Further more, the message is assumed to be a simple message, that is, with no attachments. If the application knows the messaging system and understands the format to send attachments, it can do so. The service will not flag any inconsistencies if the formatting of the message is not correct. 
     *     @param message The message to put into the mailbox.  
    @param messageInfoProperties This specifies the message information properties (names and values). 

     */
    void putMessage(String message,org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method gets a message from an open mailbox folder. The message ID can be obtained by calling the getFolderInfo and getFolderInfoProperties or embedded in an event notification from the messaging service, with information on the mailbox and notifications contained in that operation. 
@return  message 
The message associated with the messageID. 
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned.  

     */
    org.csapi.gms.IpMessage getMessage(String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,javax.slee.resource.ResourceException;


    /**
     *     This method closes a specified folder. All subfolders of the folder are also closed. 
     * 
     */
    void close() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method removes a folder from the mailbox. All subfolders of the folder are also removed. The folder must be already closed, otherwise the error code P_GMS_FOLDER_IS_OPEN is returned. If the application does not have sufficient privilege to remove the folder, the error code P_GMS_INSUFFICIENT_PRIVILEGE is returned. 
     *     @param folderID Specifies the identity of the folder. If the folder ID given is not present, the error code P_GMS_INVALID_FOLDER_ID is returned.  

     */
    void remove(String folderID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,javax.slee.resource.ResourceException;


} // IpMailboxFolderConnection

