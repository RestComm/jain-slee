package org.mobicents.csapi.jr.slee.cc.cccs;

/**
 * The conference Call Control Manager is the factory interface for creating conferences. Additionally it takes care of resource management.																											This interface shall be implemented by a Conference Call Control SCF.  As a minimum requirement, either the createConference() method shall be implemented, or the reserveResources() and freeResources() methods shall be implemented. The minimum required methods from IpMultiPartyCallControlManager are also required.
 *
 * 
 * 
 */
public interface IpConfCallControlManagerConnection extends org.mobicents.csapi.jr.slee.cc.mmccs.IpMultiMediaCallControlManagerConnection{

    /**
     * Obtains Access To a IpConfCallConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpConfCallConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.cccs.IpConfCallConnection  getIpConfCallConnection(TpConfCallIdentifier confCallIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new conference. If the specified resources are not available for the indicated duration the creation is rejected with P_RESOURCES_UNAVAILBLE.
@return conference : Specifies the interface reference and sessionID of the created conference.
     *     @param numberOfSubConferences Specifies the number of subconferences that the user wants to create automatically. The references to the interfaces of the subconferences can later be requested with getSubConferences.
The number of subconferences should be at least 1.
    @param conferencePolicy Specifies the policy to be applied for the conference, e.g., are parties allowed to join (call into) the conference?
Note that if parties are allowed to join the conference, the application can expect partyJoined() messages on the IpAppConfCall interface.
    @param numberOfParticipants Specifies the number of participants in the conference. The actual number of participants may exceed this, but these resources are not guaranteed, i.e., anything exceeding this will be best effort only and the conference service may drop or reject participants in order to fulfil other committed resource requests. By specifying 0, the application can request a best effort conference.
    @param duration Specifies the duration for which the conference resources are reserved. The duration of the conference may exceed this, but after the duration, the resources are no longer guaranteed, i.e., parties may be dropped or rejected by the service in order to satisfy other committed resource requests.  When the conference is released before the allocated duration, the reserved resources are released and can be used to satisfy other resource requests. By specifying 0, the application requests a best effort conference.

     */
    org.mobicents.csapi.jr.slee.cc.cccs.TpConfCallIdentifier createConference(int numberOfSubConferences,org.csapi.cc.cccs.TpConfPolicy conferencePolicy,int numberOfParticipants,int duration) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to check for the availability of conference resources.
The input is the search period (start and stop time and date) - mandatory.
Furthermore, a conference duration and number of participants can be specified - optional.
The search algorithm will search the specified period for availability of conference resources and tries to find an optimal solution.
When a match is found the actual number of available resources, the actual start and the actual duration for which these are available is returned. These values can exceed the requested values.
When no match is found a best effort is returned, still the actual start time, duration, number of resources are returned, but these values now indicate the best that the conference bridge can offer, e.g., one or more of these values will not reach the requested values.
@return result : Specifies the result of the search. It indicates if a match was found. If no exact match was found the best attempt is returned.
     *     @param searchCriteria Specifies the boundary conditions of the search. E.g., the time period that should be searched, the number of participants.

     */
    org.csapi.cc.cccs.TpConfSearchResult checkResources(org.csapi.cc.cccs.TpConfSearchCriteria searchCriteria) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to reserve conference resources for a given time period. Conferences can be created without first reserving resources, but in that case no guarantees can be made.
@return resourceReservation : Specifies a structured data type which contains two fields: 
ResourceID:  The address with which the conference can be addressed, both in the methods of the interface and in the network, i.e., if joinAllowed is TRUE, parties can use this address to join the conference.
If no match is found the ResourceID contains an empty address.
ReservationID: Specifies the reservation made. It should be unique in a particular resource.
     *     @param startTime Specifies the time at which the conference resources should be reserved, i.e., the start time of the conference.
    @param numberOfParticipants Specifies the number of participants in the conference. The actual number of participants may exceed this, but these resources are not guaranteed, i.e., anything exceeding this will be best effort only and the conference service may drop or reject participants in order to fulfil other committed resource requests.
    @param duration Specifies the duration for which the conference resources are reserved. The duration of the conference may exceed this, but after the duration, the resources are no longer guaranteed, i.e., parties may be dropped or rejected by the service in order to satisfy other committed resource requests. When the conference is released before the allocated duration, the reserved resources are released and can be used to satisfy other resource requests.
    @param conferencePolicy The policy to be applied for the conference, e.g., are parties allowed to join (call into) the conference?  Note that if parties are allowed to join the conference, the application can expect partyJoined() messages on the appConfCall.

     */
    org.csapi.cc.cccs.TpResourceReservation reserveResources(String startTime,int numberOfParticipants,int duration,org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method can be used to cancel an earlier made reservation of conference resources.
This also means that no ConferenceCreated events will be received for this conference.
     *     @param resourceReservation Specifies the ResourceID and the ReservationID that were received during the reservation.

     */
    void freeResources(org.csapi.cc.cccs.TpResourceReservation resourceReservation) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpConfCallControlManagerConnection

