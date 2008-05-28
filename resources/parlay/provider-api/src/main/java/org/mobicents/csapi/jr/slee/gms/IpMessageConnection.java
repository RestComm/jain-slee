package org.mobicents.csapi.jr.slee.gms;

/**
 * This class represents the < Parlay IpMessageConnection > interface
 *
 * 
 * 
 */
public interface IpMessageConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method returns the number of message information properties of the specified message. 
@return  numberOfProperties 
The number of properties associated with the message. The application can then use the information contained to decide whether to get the message or the message information properties from a mailbox folder. The number of properties is zero or positive. 
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned. 

     */
    int getInfoAmount(String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,javax.slee.resource.ResourceException;


    /**
     *     This method returns the properties of a message. 
@return  messageInfoProperties 
The message information properties (names and values) present in the message. Message properties include message format, read/unread, sent/unsent, message size, relevant dates and times, subject and addresses. 
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned. 
    @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be retrieved from the message is located. Properties are numbered from zero. 
    @param numberOfProperties The number of properties to return. If the value of this parameter is zero, then all properties will be returned. Otherwise, the value must be a positive number. If the number is not positive, the error code P_GMS_NUMBER_NOT_POSITIVE is returned. 

     */
    org.csapi.gms.TpMessageInfoProperty[] getInfoProperties(String messageID,int firstProperty,int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,javax.slee.resource.ResourceException;


    /**
     *     This method sets the properties of a message. 
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned. 
    @param firstProperty This is the first property of interest.  This number represents the starting point where the first property of the list to be retrieved from the message is located. Properties are numbered from zero. 
    @param messageInfoProperties This specifies the message information properties (names and values) to be set in the message. Message properties that may be changed include read/unread status, subject and importance. If the properties cannot be changed, then the error code P_GMS_PROPERTY_NOT_SET is returned. 

     */
    void setInfoProperties(String messageID,int firstProperty,org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,javax.slee.resource.ResourceException;


    /**
     *     This method removes a message from the open mailbox folder. If the application does not have sufficient privilege to remove the message, the error code P_GMS_INSUFFICIENT_PRIVILEGE is returned. 
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned.
The message ID can be obtained by calling the getFolderInfo and getFolderInfoProperties or embedded in an event notification from the messaging service, with information on the mailbox and notifications contained in that operation. If the message cannot be removed, the error code P_GMS_MESSAGE_NOT_REMOVED is returned.

     */
    void remove(String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE,org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,javax.slee.resource.ResourceException;


    /**
     *     This method retrieves the message content.
@return  content 
@return the message content.
     *     @param messageID Specifies the identity of the message. If the message ID given is not present, the error code P_GMS_INVALID_MESSAGE_ID is returned.

     */
    String getContent(String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,javax.slee.resource.ResourceException;


} // IpMessageConnection

