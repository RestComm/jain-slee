package org.mobicents.csapi.jr.slee.cc.mpccs;

/**
 * The call leg interface represents the logical call leg associating a call with an address. The call leg tracks its own states and allows charging summaries to be accessed. The leg represents the signalling relationship between the call and an address.  An application that uses the IpCallLeg interface to set up connections has good control, e.g. by defining leg specific event request and can obtain call leg specific report and events.													This interface shall be implemented by a Multi Party Call Control SCF.  The routeReq(), eventReportReq(), release(), continueProcessing() and deassign() methods shall be implemented as a minimum requirement. 
 *
 * 
 * 
 */
public interface IpCallLegConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This asynchronous method requests routing of the call leg to the remote party indicated by the targetAddress.
In case the connection to the destination party is established successfully the CallLeg will be either detached or attached to the call based on the attach Mechanism values specified in the connectionProperties parameter.
The extra address information such as originatingAddress is optional. If not present (i.e. the plan is set to P_ADDRESS_PLAN_NOT_PRESENT), the information provided in the corresponding addresses from the route is used, otherwise network or gateway provided addresses will be used.
If the application wishes that the call leg should be represented in the network as being a redirection it should include a value for the field P_CALL_APP_ORIGINAL_DESTINATION_ADDRESS of TpCallAppInfo.
This operation continues processing of the call leg.
Note that for application initiated calls in some networks the result of the first routeReq() has to be received before the next routeReq() can be invoked. The Service Property P_PARALLEL_INITIAL_ROUTING_REQUESTS (see section 7.5) indicates how a specific implementation handles the initial routeReq().This method shall throw P_TASK_REFUSED if an application is not allowed to use parallel routing requests.
     *     @param targetAddress Specifies the destination party to which the call leg should be routed.
    @param originatingAddress Specifies the address of the originating (calling) party.
    @param connectionProperties Specifies the properties of the connection.

     */
    void routeReq(org.csapi.TpAddress targetAddress,org.csapi.TpAddress originatingAddress,org.csapi.cc.TpCallAppInfo[] appInfo,org.csapi.cc.TpCallLegConnectionProperties connectionProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method sets, clears or changes the criteria for the events that the call leg object will be set to observe.
     *     @param eventsRequested Specifies the event specific criteria used by the application to define the events required. Only events that meet these criteria are reported. Examples of events are "address analysed", "answer" and "release".

     */
    void eventReportReq(org.csapi.cc.TpCallEventRequest[] eventsRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_CRITERIA,javax.slee.resource.ResourceException;


    /**
     *     This method requests the release of the call leg. If successful, the associated address (party) will be released from the call, and the call leg deleted. Note that in some cases releasing the party may lead to release of the complete call in the network. The application will be informed of this with callEnded().
This operation continues processing of the call leg.
     *     @param cause Specifies the cause of the release.

     */
    void release(org.csapi.cc.TpReleaseCause cause) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method requests information associated with the call leg to be provided at the appropriate time (for example, to calculate charging). Note that in the call leg information must be accessible before the objects of concern are deleted.
     *     @param callLegInfoRequested Specifies the call leg information that is requested.

     */
    void getInfoReq(int callLegInfoRequested) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method requests the call associated with this call leg.
@return callReference: Specifies the interface and sessionID of the call associated with this call leg.
     * 
     */
    org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier getCall() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the call leg be attached to its call object. This will allow transmission on all associated bearer connections or media streams to and from other parties in the call. The call leg must be in the connected state for this method to complete successfully.
In case this method is invoked while there is still a request to detach the Media pending, the exception "P_TASK_REFUSED" will be raised.
     * 
     */
    void attachMediaReq() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method will detach the call leg from its call, i.e., this will prevent transmission on any associated bearer connections or media streams to and from other parties in the call. The call leg must be in the connected state for this method to complete successfully.
In case this method is invoked while there is still a request to attach the Media pending, the exception "P_TASK_REFUSED" will be raised.
     * 
     */
    void detachMediaReq() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     Queries the current address of the destination  the leg has been directed to.
@return the address of the destination point towards which the call leg has been routed..
If this method is invoked on the Originating Call Leg, exception P_INVALID_STATE will be thrown. 
     * 
     */
    org.csapi.TpAddress getCurrentDestinationAddress() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This operation continues processing of the call leg. Applications can invoke this operation after call leg processing was interrupted due to detection of a notification or event the application subscribed its interest in.
In case the operation is invoked and call leg processing is not interrupted the exception P_INVALID_NETWORK_STATE will be raised.
     * 
     */
    void continueProcessing() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     Set an operator specific charge plan for the call leg.
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
     *     The application calls this method to supervise a call leg. The application can set a granted connection time for this call. If an application calls this function before it calls a routeReq() or a user interaction function the time measurement will start as soon as the call is answered by the B-party or the user interaction system.
     *     @param time Specifies the granted time in milliseconds for the connection. Measurement will start as soon as the callLeg is connected in the network.
    @param treatment Specifies how the network should react after the granted connection time expired.

     */
    void superviseReq(int time,int treatment) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the relationship between the application and the call leg  and associated objects be de-assigned. It leaves the call leg in progress, however, it purges the specified call leg object so that the application has no further control of call leg processing. If a call leg is de-assigned that has event reports or call leg information reports requested, then these reports will be disabled and any related information discarded.
The application should not release or deassign the call leg when received a callLegEnded() or callEnded(). This operation continues processing of the call leg.
When this method is invoked, all outstanding supervision requests will be cancelled.
     * 
     */
    void deassign() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpCallLegConnection

