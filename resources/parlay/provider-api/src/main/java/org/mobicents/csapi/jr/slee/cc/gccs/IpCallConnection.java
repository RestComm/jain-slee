package org.mobicents.csapi.jr.slee.cc.gccs;

/**
 * The generic Call provides the possibility to control the call routing, to request information from the call, control the charging of the call, to release the call and to supervise the call.  It does not give the possibility to control the legs directly and it does not allow control over the media. The first capability is provided by the multi-party call and the latter as well by the multi-media call.  The call is limited to two party calls, although it is possible to provide 'follow-on' calls, meaning that the call can be rerouted after the terminating party has disconnected or routing to the terminating party has failed. Basically, this means that at most two legs can be in connected or routing state at any time.			This interface shall be implemented by a Generic Call Control SCF.  As a minimum requirement, the routeReq (), release() and deassignCall() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpCallConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This asynchronous method requests routing of the call to the remote party indicated by the targetAddress. 
Note that in case of routeReq() it is recommended to request for 'successful' (e.g. 'answer' event) and  'failure' events at invocation, because those are needed for the application to keep track of the state of the call.
The extra address information such as originatingAddress is optional. If not present (i.e., the plan is set to P_ADDRESS_PLAN_NOT_PRESENT), the information provided in corresponding addresses from the route is used, otherwise the network or gateway provided numbers will be used.
If this method in invoked, and call reports have been requested, yet no IpAppCall interface has been provided, this method shall throw the P_NO_CALLBACK_ADDRESS_SET exception.
This operation continues processing of the call implicitly.
@return callLegSessionID: Specifies the sessionID assigned by the gateway. This is the sessionID of the implicitly created call leg. The same ID will be returned in the routeRes or Err. This allows the application to correlate the request and the result.
This parameter is only relevant when multiple routeReq() calls are executed in parallel, e.g., in the multi-party call control service.
     *     @param responseRequested Specifies the set of observed events that will result in zero or more  routeRes() being generated.
E.g., when both answer and disconnect is monitored the result can be received two times. 
If the application wants to control the call (in whatever sense) it shall enable event reports
    @param targetAddress Specifies the destination party to which the call leg should be routed.
    @param originatingAddress Specifies the address of the originating (calling) party.
    @param originalDestinationAddress Specifies the original destination address of the call.
    @param redirectingAddress Specifies the address from which the call was last redirected.

     */
    int routeReq(org.csapi.cc.gccs.TpCallReportRequest[] responseRequested,org.csapi.TpAddress targetAddress,org.csapi.TpAddress originatingAddress,org.csapi.TpAddress originalDestinationAddress,org.csapi.TpAddress redirectingAddress,org.csapi.cc.gccs.TpCallAppInfo[] appInfo) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method requests the release of the call object and associated objects. The call will also be terminated in the network. If the application requested reports to be sent at the end of the call (e.g., by means of getCallInfoReq) these reports will still be sent to the application.
The application should always either release or deassign the call when it is finished with the call, unless a callFaultDetected is received by the application.
This operation continues processing of the call implicitly.
     *     @param cause Specifies the cause of the release.

     */
    void release(org.csapi.cc.gccs.TpCallReleaseCause cause) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the relationship between the application and the call and associated objects be de-assigned. It leaves the call in progress, however, it purges the specified call object so that the application has no further control of call processing. If a call is de-assigned that has event reports, call information reports or call Leg information reports requested, then these reports will be disabled and any related information discarded.
The application should always either release or deassign the call when it is finished with the call, unless callFaultDetected is received by the application.
This operation continues processing of the call implicitly.
     * 
     */
    void deassignCall() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method requests information associated with the call to be provided at the appropriate time (for example, to calculate charging). This method must be invoked before the call is routed to a target address. 
A report is received when the destination leg or party terminates or when the call ends. The call object will exist after the call is ended if information is required to be sent to the application at the end of the call. In case the originating party is still available the application can still initiate a follow-on call using routeReq.
     *     @param callInfoRequested Specifies the call information that is requested.

     */
    void getCallInfoReq(int callInfoRequested) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     Set an operator specific charge plan for the call.
     *     @param callChargePlan Specifies the charge plan to use.

     */
    void setCallChargePlan(org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method allows for advice of charge (AOC) information to be sent to terminals that are capable of receiving this information.
     *     @param aOCInfo Specifies two sets of Advice of Charge parameter.
    @param tariffSwitch Specifies the tariff switch interval that signifies when the second set of AoC parameters becomes valid.

     */
    void setAdviceOfCharge(org.csapi.TpAoCInfo aOCInfo,int tariffSwitch) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This asynchronous method requests the call control service to collect further digits and return them to the application. Depending on the administered data, the network may indicate a new call to the gateway if a caller goes off-hook or dialled only a few digits. The application then gets a new call event which contains no digits or only the few dialled digits in the event data. 
The application should use this method if it requires more dialled digits, e.g. to perform screening.
     *     @param length Specifies the maximum number of digits to collect. 

     */
    void getMoreDialledDigitsReq(int length) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     The application calls this method to supervise a call. The application can set a granted connection time for this call. If an application calls this function before it calls a routeReq() or a user interaction function the time measurement will start as soon as the call is answered by the B-party or the user interaction system.
     *     @param time Specifies the granted time in milliseconds for the connection.
    @param treatment Specifies how the network should react after the granted connection time expired.

     */
    void superviseCallReq(int time,int treatment) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This operation continues processing of the call explicitly. Applications can invoke this operation after call processing was interrupted due to detection of a notification or event the application subscribed its interest in. 
In case the operation is invoked and call processing is not interrupted the exception P_INVALID_NETWORK_STATE will be raised.
     * 
     */
    void continueProcessing() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


} // IpCallConnection

