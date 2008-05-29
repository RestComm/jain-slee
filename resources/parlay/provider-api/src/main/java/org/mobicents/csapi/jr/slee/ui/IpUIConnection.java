package org.mobicents.csapi.jr.slee.ui;

/**
 * The User Interaction Service Interface provides functions to send information to, or gather information from the user. An application can use the User Interaction Service Interface independently of other services.  							This interface, or the IpUICall interface, shall be implemented by a Generic User Interaction SCF as a minimum requirement.  The release() method, and at least one of the sendInfoReq() or the sendInfoAndCollectReq() methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpUIConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This asynchronous method plays an announcement or sends other information to the user.
@return  assignmentID 
Specifies the ID assigned by the generic user interaction interface for a user interaction request. 
     *     @param info Specifies the information to send to the user. This information can be: 
- an infoID, identifying pre-defined information to be sent (announcement and/or text);
- a string, defining the text to be sent;
- a URL , identifying pre-defined information or data to be sent to or downloaded into the terminal.  A URL enables the application to utilize dynamic multi-media content by reference.
- Binary Data, identifying pre-defined information or data to be sent to or downloaded into the terminal.  Binary data enables the application to utilize dynamic multi-media content directly.
    @param language Specifies the Language of the information to be sent to the user.
    @param variableInfo  Defines the variable part of the information to send to the user.
    @param repeatIndicator Defines how many times the information shall be sent to the end-user. A value of zero (0) indicates that the announcement shall be repeated until the call or call leg is released or an abortActionReq() is sent.
    @param responseRequested Specifies if a response is required from the call user interaction service, and any action the service should take. 

     */
    int sendInfoReq(org.csapi.ui.TpUIInfo info,String language,org.csapi.ui.TpUIVariableInfo[] variableInfo,int repeatIndicator,int responseRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.ui.P_ILLEGAL_ID,org.csapi.ui.P_ID_NOT_FOUND,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method plays an announcement or sends other information to the user and collects some information from the user. The announcement usually prompts for a number of characters (for example, these are digits or text strings such as "YES" if the user's terminal device is a phone).
@return  assignmentID 
Specifies the ID assigned by the generic user interaction interface for a user interaction request.
     *     @param info Specifies the ID of the information to send to the user. This information can be: 
- an infoID, identifying pre-defined information to be sent (announcement and/or text);
- a string, defining the text to be sent;
- a URL , identifying pre-defined information or data to be sent to or downloaded into the terminal.  A URL enables the application to utilize dynamic multi-media content by reference.
- Binary Data, identifying pre-defined information or data to be sent to or downloaded into the terminal.  Binary data enables the application to utilize dynamic multi-media content directly.

    @param language Specifies the Language of the information to be sent to the user.
    @param variableInfo Defines the variable part of the information to send to the user. 
    @param criteria Specifies additional properties for the collection of information, such as the maximum and minimum number of characters, end character, first character timeout and inter-character timeout. 
    @param responseRequested Specifies if a response is required from the call user interaction service, and any action the service should take. For this case it can especially be used to indicate e.g. the final request.  If  P_UI_RESPONSE_REQUIRED is not enabled by the application request, the user interaction shall nevertheless return either a sendInfoAndCollectRes or sendInfoAndCollectErr method to the application in response to this method invocation.

     */
    int sendInfoAndCollectReq(org.csapi.ui.TpUIInfo info,String language,org.csapi.ui.TpUIVariableInfo[] variableInfo,org.csapi.ui.TpUICollectCriteria criteria,int responseRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.ui.P_ILLEGAL_ID,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.ui.P_ILLEGAL_RANGE,org.csapi.ui.P_INVALID_COLLECTION_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the relationship between the application and the user interaction object be released. It causes the release of the used user interaction resources and interrupts any ongoing user interaction.
     * 
     */
    void release() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method sets the originating address property on the user interaction session to be used when sending information to the user.
     *     @param origin Specifies the originating address.  The originating address description is sent as a TpString. However this field may contain E.164 addresses that the receiving terminal can use to reply to the message. The coding of such an E.164 address can either be local numbers or international numbers, according to the standard E.164. Examples for a local number is "0702106181" and for an international number "+46702106181".

     */
    void setOriginatingAddress(String origin) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_ADDRESS,javax.slee.resource.ResourceException;


    /**
     *     This method gets the originating address property on the user interaction session to be used when sending information to the user.  If not set with setOriginatingAddress(), the getOriginatingAddress() returns the description that would be displayed on the terminal device as the originating address when a message is sent with sendInfoReq() or sendInfoAndCollectReq().
@return  TpString
The address that will be used for a sendInfoReq() or sendInfoAndCollectReq() for the originating address. 
     * 
     */
    String getOriginatingAddress() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


} // IpUIConnection

