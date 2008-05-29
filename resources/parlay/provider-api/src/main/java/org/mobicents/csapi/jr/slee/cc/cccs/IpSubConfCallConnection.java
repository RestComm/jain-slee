package org.mobicents.csapi.jr.slee.cc.cccs;

/**
 * The subconference is an additional grouping mechanism within a conference. Parties (legs) that are in the same subconference have a speech connection with each other.  The following inherited call methods apply to the subconference as a whole, with the specified semantics:																			-  setCallback; changes the callback interface reference.																		-  release; releases the subconference, including all currently attached legs. When the last subconference in the conference is released, the conference is implicitly released as well.															-  deassignCall; de-assigns the subconference. No callbacks will be received by the application on this subconference, nor will the gateway accept any methods on this subconference or accept any methods using the subconference as a parameter (e.g., merge). When the subconference is the last subconference in the conference, the conference is deassigned as well. In general it is recommended to only use deassignCall for the complete conference.		-  getInfoReq; request information over the subconference. The subconference duration is defined as the time when the first party joined the subconference until when the last party leaves the subconference or the subconference is released.																																-  setChargePlan; set the charge plan for the subconference.																	-  superviseReq; supervise the duration of the subconference. It is recommended that this method is only used on the complete conference.																												- superviseVolumeReq; supervises and sets a granted data volume for the subconference.								-  getCallLegs; return all the call legs in the subconference.																	-  createCallLeg; create a call leg.																								-  createAndRouteCallLegReq; implicitly create a leg and route the leg to the specified destination. 																																					This interface shall be implemented by a Conference Call Control SCF.  As a minimum requirement, either the moveCallLeg() method shall be implemented, or the splitSubConference() and mergeSubConference() methods shall be implemented. The minimum required methods from IpMultiPartyCall are also required.
 *
 * 
 * 
 */
public interface IpSubConfCallConnection extends org.mobicents.csapi.jr.slee.cc.mmccs.IpMultiMediaCallConnection{

    /**
     * Obtains Access To a IpSubConfCallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpSubConfCallConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.cccs.IpSubConfCallConnection  getIpSubConfCallConnection(TpSubConfCallIdentifier subConfCallIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new subconference and move some of the legs to it.
@return newSubConferenceCall : Specifies the new subconference that is implicitly created as a result of the method.
     *     @param callLegList Specifies the sessionIDs of the legs that will be moved to the new subconference.

     */
    org.mobicents.csapi.jr.slee.cc.cccs.TpSubConfCallIdentifier splitSubConference(int[] callLegList) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to merge two subconferences, i.e., move all our legs from this subconference to the other subconference followed by a release of this subconference.
     *     @param targetSubConferenceCall The session ID of  target subconference with which the current subconference will be merged.

     */
    void mergeSubConference(int targetSubConferenceCall) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method moves one leg from this subconference to another subconference.
     *     @param targetSubConferenceCall Specifies the sessionID of the target subconference.
    @param callLeg Specifies the sessionID of the call leg to be moved.

     */
    void moveCallLeg(int targetSubConferenceCall,int callLeg) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method can be used by the application to select which video should be sent to the party that is currently selected as the chair.  
Whether this method can be used depends on the selected conference policy.
     *     @param inspectedCallLeg Specifies the sessionID of call leg of the party whose video stream should be sent to the chair.

     */
    void inspectVideo(int inspectedCallLeg) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method cancels a previous inspectVideo. The chair will receive the broadcasted video.
Whether this method can be used depends on the selected conference policy.
     * 
     */
    void inspectVideoCancel() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method indicates which of the participants in the conference has the floor. The video of the speaker will be broadcast to the other parties.
Whether this method can be used depends on the selected conference policy.
     *     @param speakerCallLeg Specifies the sessionID of the call leg of the party whose video stream should be broadcast.

     */
    void appointSpeaker(int speakerCallLeg) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to indicate which participant in the conference is the chair. E.g., the terminal of this participant will be the destination of the video of the inspectVideo method.
Whether this method can be used depends on the selected conference policy.
     *     @param chairCallLeg Specifies the sessionID of the call leg of the party that will become the chair.

     */
    void chairSelection(int chairCallLeg) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method can be used to change the conference policy in an ongoing conference.
Multi media conference policy options available. E.g.:
- chair controlled video / voice switched video;
- closed conference / open conference;
- Composite video (different types) / only speaker.
     *     @param conferencePolicy New Conference Policy to be used in the subconference.

     */
    void changeConferencePolicy(org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpSubConfCallConnection

