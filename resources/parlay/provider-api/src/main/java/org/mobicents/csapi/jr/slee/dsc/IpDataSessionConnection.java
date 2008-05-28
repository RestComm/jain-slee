package org.mobicents.csapi.jr.slee.dsc;

/**
 * The Data Session interface provides basic methods for applications to control data sessions.  This interface shall be implemented by a Data Session Control SCF.  As a minimum requirement, the connectReq(), release(), deassignDataSession() and continueProcessing() methods shall be implemented.
 *
 * 
 * 
 */
public interface IpDataSessionConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This asynchronous method requests the connection of a data session with the destination party (specified in the parameter TargetAddress).  The Data Session object is not automatically deleted if the destination party disconnects from the data session. 
@return assignmentID : Specifies the ID assigned to the request. The same ID will be returned in the connectRes or Err. This allows the application to correlate the request and the result.
     *     @param responseRequested Specifies the set of observed data session events that will result in a connectRes() being generated.
    @param targetAddress Specifies the address of destination party. 

     */
    int connectReq(org.csapi.dsc.TpDataSessionReportRequest[] responseRequested,org.csapi.TpAddress targetAddress) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_ADDRESS,javax.slee.resource.ResourceException;


    /**
     *     This method requests the release of the data session and associated objects.
     *     @param cause Specifies the cause of the release. 

     */
    void release(org.csapi.dsc.TpDataSessionReleaseCause cause) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     The application calls this method to supervise a data session. The application can set a granted data volume for this data session. If an application calls this function before it calls a connectReq() or a user interaction function the time measurement will start as soon as the data session is connected. The Data Session object will exist after the data session has been terminated if information is required to be sent to the application at the end of the data session.
     *     @param treatment Specifies how the network should react after the granted data volume has been sent. 
    @param bytes Specifies the granted number of bytes that can be transmitted for the data session.  

     */
    void superviseDataSessionReq(int treatment,org.csapi.dsc.TpDataSessionSuperviseVolume bytes) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     Allows an application to include charging information in network generated CDR.
     *     @param dataSessionChargePlan Specifies the charge plan used. 

     */
    void setDataSessionChargePlan(org.csapi.dsc.TpDataSessionChargePlan dataSessionChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


    /**
     *     This method allows the application to determine the charging information that will be sent to the end-users terminal.
     *     @param aoCInfo Specifies two sets of Advice of Charge parameter according to GSM. 
    @param tariffSwitch Specifies the tariff switch that signifies when the second set of AoC parameters becomes valid.

     */
    void setAdviceOfCharge(org.csapi.TpAoCInfo aoCInfo,int tariffSwitch) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT,javax.slee.resource.ResourceException;


    /**
     *     This method requests that the relationship between the application and the data session and associated objects be de-assigned. It leaves the data session in progress, however, it purges the specified data session object so that the application has no further control of data session processing. If a data session is de-assigned that has event reports, data session information reports requested, then these reports will be disabled and any related information discarded.
The application should always either release or deassign the data session when it is finished with the data session, unless dataSessionFaultDetected is received by the application. 
     * 
     */
    void deassignDataSession() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This operation continues processing of the data session. Applications can invoke this operation after session handling was interrupted due to detection of a notification or event the application subscribed its interest in.
     * 
     */
    void continueProcessing() throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_NETWORK_STATE,javax.slee.resource.ResourceException;


} // IpDataSessionConnection

