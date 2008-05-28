package org.mobicents.csapi.jr.slee.cc.cccs;

/**
 * The conference call manages the subconferences. It also provides some convenience methods to hide the fact of multiple subconferences from the applications that do not need it.  Note that the conference call always contains one subconference.  The following inherited call methods apply to the conference as a whole, with the specified semantics:  																																		-  setCallback; changes the callback interface reference. 																		-  release; releases the entire conference, including all the subconferences and detached legs.  							-  deassignCall; de-assigns the complete conference. No callbacks will be received by the application, either on the conference, or on any of the contained subconferences or call legs.  															-  getInfoReq; request information over the complete conference. The conference duration is defined as the time when the first party joined the conference until when the last party leaves the conference or the conference is released. 																																		-  setChargePlan; set the chargeplan for the conference. This chargeplan will apply to all the subconferences, unless another chargeplan is explicitly overridden on the subconference. 																-  superviseReq; supervise the duration of the complete conference.  														-  getCallLegs; return all the call legs used within the conference.  															- superviseVolumeReq; supervises and sets a granted data volume for the conference.																																									Other methods apply to the default subconference. When using multiple subconferences, it is recommended that the application calls these methods directly on the subconference since this makes it more explicit what the effect of the method is:  																															-  createAndRouteCallLegReq																									-  createCallLeg																																																													This interface shall be implemented by a Conference Call Control SCF.  As a minimum requirement, the getSubConferences(), getConferenceAddress() and createSubConference() methods shall be implemented. The minimum required methods from IpMultiPartyCall are also required.
 *
 * 
 * 
 */
public interface IpConfCallConnection extends org.mobicents.csapi.jr.slee.cc.mmccs.IpMultiMediaCallConnection{


    /**
     *     This method returns all the subconferences of the conference.
@return subConferenceList : Specifies the list of all the subconferences of the conference.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.cccs.TpSubConfCallIdentifier[] getSubConferences() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;

    /**
     * Obtains Access To a IpSubConfCallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpSubConfCallConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.cccs.IpSubConfCallConnection  getIpSubConfCallConnection(TpSubConfCallIdentifier subConfCallIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new subconference. Note that one subconference is already created together with the conference.
@return subConference : Specifies the created subconference (interface and sessionID).
     *     @param conferencePolicy Conference Policy to be used in the subconference.  Optional; if undefined, the policy of the conference is used. Note that not all policy elements have to be applicable for subconferences.

     */
    org.mobicents.csapi.jr.slee.cc.cccs.TpSubConfCallIdentifier createSubConference(org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to request a notification when a party leaves the conference.
     * 
     */
    void leaveMonitorReq() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method returns the conference address that specifies the address with which the conference can be addressed in case parties are allowed to join the conference.
     * 
     */
    org.csapi.TpAddress getConferenceAddress() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpConfCallConnection

