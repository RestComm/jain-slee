package org.mobicents.csapi.jr.slee.cc.mpccs;

/**
 * The Multi-Party Call provides the possibility to control the call routing, to request information from the call, control the charging of the call, to release the call and to supervise the call.  It also gives the possibility to manage call legs explicitly.  An application may create more then one call leg. 																	This interface shall be implemented by a Multi Party Call Control SCF.  The release() and deassignCall() methods, and either the createCallLeg() or the createAndRouteCallLegReq(), shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpMultiPartyCallConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method requests the identification of the call leg objects associated with the call object. @return the legs in the order of creation.
@return callLegList: Specifies the call legs associated with the call. The set contains both the sessionIDs and the interface references.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[] getCallLegs() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;

    /**
     * Obtains Access To a IpCallLegConnection interface
     * @exception ResourceException If it is not possible to create the connection
     * @return A IpCallLegConnection interface
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection  getIpCallLegConnection(TpCallLegIdentifier callLegIdentifier) throws javax.slee.resource.ResourceException;


    /**
     *     This method requests the creation of a new call leg object.
@return callLeg: Specifies the interface and sessionID of the call leg created.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier createCallLeg() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous operation requests creation and routing of a new callLeg. In case the connection to the destination party is established successfully the CallLeg is attached to the call, i.e. no explicit attachMediaReq() operation is needed. Requested events will be reported on the IpAppCallLeg interface. This interface the application must provide through the appLegInterface parameter. 
The extra address information such as originatingAddress is optional. If not present (i.e., the plan is set to P_ADDRESS_PLAN_NOT_PRESENT), the information provided in corresponding addresses from the route is used, otherwise the network or gateway provided numbers will be used.
If the application wishes that the call leg should be represented in the network as being a redirection it should include a value for the field P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS of TpCallAppInfo.
If this method is invoked, and call reports have been requested, yet the IpAppCallLeg interface parameter is NULL, this method shall throw the P_NO_CALLBACK_ADDRESS_SET exception.
Note that for application initiated calls in some networks the result of the first createAndRouteCallLegReq() has to be received before the next createAndRouteCallLegReq() can be invoked. The Service Property P_PARALLEL_INITIAL_ROUTING_REQUESTS (see section 7.5) indicates how a specific implementation handles the initial createAndRouteCallLegReq(). This method shall throw P_TASK_REFUSED if an application is not allowed to use parallel routing requests.
@return callLegReference: Specifies the reference to the CallLeg interface that was created.
     *     @param eventsRequested Specifies the event specific criteria used by the application to define the events required. Only events that meet these criteria are reported. Examples of events are "address analysed", "answer" and "release".	
    @param targetAddress Specifies the destination party to which the call should be routed.
    @param originatingAddress Specifies the address of the originating (calling) party.

     */
    org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier createAndRouteCallLegReq(org.csapi.cc.TpCallEventRequest[] eventsRequested,org.csapi.TpAddress targetAddress,org.csapi.TpAddress originatingAddress,org.csapi.cc.TpCallAppInfo[] appInfo) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method requests the release of the call object and associated objects. The call will also be terminated in the network. If the application requested reports to be sent at the end of the call (e.g,, by means of getInfoReq) these reports will still be sent to the application.
     *     @param cause Specifies the cause of the release.

     */
    void release(org.csapi.cc.TpReleaseCause cause) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the relationship between the application and the call and associated objects be de-assigned. It leaves the call in progress, however, it purges the specified call object so that the application has no further control of call processing. If a call is de-assigned that has call information reports, call leg event reports or call Leg information reports requested, then these reports will be disabled and any related information discarded.
When this method is invoked, all outstanding supervision requests will be cancelled.
     * 
     */
    void deassignCall() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method requests information associated with the call to be provided at the appropriate time (for example, to calculate charging). This method must be invoked before the call is routed to a target address.
A report is received when the destination leg or party terminates or when the call ends. The call object will exist after the call is ended if information is required to be sent to the application at the end of the call. In case the originating party is still available the application can still initiate a follow-on call using routeReq.
     *     @param callInfoRequested Specifies the call information that is requested.

     */
    void getInfoReq(int callInfoRequested) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     Set an operator specific charge plan for the call.
     *     @param callChargePlan Specifies the charge plan to use.

     */
    void setChargePlan(org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method allows for advice of charge (AOC) information to be sent to terminals that are capable of receiving this information.
     *     @param aOCInfo Specifies two sets of Advice of Charge parameter.
    @param tariffSwitch Specifies the tariff switch interval that signifies when the second set of AoC parameters becomes valid.

     */
    void setAdviceOfCharge(org.csapi.TpAoCInfo aOCInfo,int tariffSwitch) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CURRENCY,org.csapi.P_INVALID_AMOUNT,javax.slee.resource.ResourceException;


    /**
     *     The application calls this method to supervise a call. The application can set a granted connection time for this call. If an application calls this operation before it routes a call or a user interaction operation the time measurement will start as soon as the call is answered by the B-party or the user interaction system.
     *     @param time Specifies the granted time in milliseconds for the connection.  Measurement will start as soon as the call is connected in the network, e.g, answered by the B-party or the user-interaction system.
    @param treatment Specifies how the network should react after the granted connection time expired.

     */
    void superviseReq(int time,int treatment) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiPartyCallConnection

