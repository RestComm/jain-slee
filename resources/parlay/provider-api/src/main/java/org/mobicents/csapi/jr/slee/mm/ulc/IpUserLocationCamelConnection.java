package org.mobicents.csapi.jr.slee.mm.ulc;

/**
 * This interface is the 'service manager' interface for ULC.
This interface shall be implemented by a User Location Camel SCF.
The locationReportReq() method, or both the periodicLocationReportingStartReq() and periodicLocationReportingStop() methods, or both the triggeredLocationReportingStartReq() and triggeredLocationReportingStop() methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpUserLocationCamelConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     Request for mobile-related location information on one or several camel users.
@return  assignmentId 
Specifies the assignment ID of the location-report request.
     *     @param users Specifies the user(s) for which the location shall be reported.

     */
    int locationReportReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Request for periodic mobile location reports on one or several users.
@return  assignmentId 
Specifies the assignment ID of the periodic location-reporting request.
     *     @param users Specifies the user(s) for which the location shall be reported.
    @param reportingInterval Specifies the requested interval in seconds between the reports.

     */
    int periodicLocationReportingStartReq(org.csapi.TpAddress[] users,int reportingInterval) throws org.csapi.TpCommonExceptions,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     This method stops the sending of periodic mobile location reports for one or several users.
     *     @param stopRequest Specifies how the assignment shall be stopped, i.e. if whole or just parts of the assignment should be stopped.

     */
    void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


    /**
     *     Request for user location reports, containing mobile related information, when the location is changed (the report is triggered by the location change).
@return  assignmentId 
Specifies the assignment ID of the triggered location-reporting request.
     *     @param users Specifies the user(s) for which the location shall be reported.
    @param trigger Specifies the trigger conditions.

     */
    int triggeredLocationReportingStartReq(org.csapi.TpAddress[] users,org.csapi.mm.TpLocationTriggerCamel trigger) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Request that triggered mobile location reporting should stop.
     *     @param stopRequest Specifies how the assignment shall be stopped, i.e. if whole or just parts of the assignment should be stopped.

     */
    void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpUserLocationCamelConnection

