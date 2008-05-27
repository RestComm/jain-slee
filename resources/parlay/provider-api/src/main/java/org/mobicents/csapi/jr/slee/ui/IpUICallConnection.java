package org.mobicents.csapi.jr.slee.ui;

/**
 * The Call User Interaction Service Interface provides functions to send information to, or gather information from the user (or call party) to which a call leg is connected.  An application can use the Call User Interaction Service Interface only in conjunction with another service interface, which provides mechanisms to connect a call leg to a user. At present, only the Call Control service supports this capability.  																This interface, or the IpUI interface, shall be implemented by a Generic User Interaction SCF as a minimum requirement.  The minimum required methods of interface IpUI shall be implemented.
 *
 * 
 * 
 */
public interface IpUICallConnection extends org.mobicents.csapi.jr.slee.ui.IpUIConnection{


    /**
     *     This asynchronous method allows the recording of a message. The recorded message can be played back at a later time with the sendInfoReq() method.
@return  assignmentID 
Specifies the ID assigned by the generic user interaction interface for a user interaction request.
     *     @param info Specifies the information to send to the user. This information can be either an ID (for pre-defined announcement or text), a text string, or an URL (indicating the information to be sent, e.g. an audio stream). 
    @param criteria  Defines the criteria for recording of messages

     */
    int recordMessageReq(org.csapi.ui.TpUIInfo info,org.csapi.ui.TpUIMessageCriteria criteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.ui.P_ILLEGAL_ID,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method allows to delete a recorded message.
@return  assignmentID 
Specifies the ID assigned by the generic user interaction interface for a user interaction request.
     *     @param messageID Specifies the message ID.

     */
    int deleteMessageReq(int messageID) throws org.csapi.TpCommonExceptions,org.csapi.ui.P_ILLEGAL_ID,org.csapi.ui.P_ID_NOT_FOUND,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method aborts a user interaction operation, e.g. a sendInfoReq(), from the specified call leg. The call and call leg are otherwise unaffected. The user interaction call service interrupts the current action on the specified leg. 
     *     @param assignmentID Specifies the user interaction request to be cancelled.

     */
    void abortActionReq(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpUICallConnection

