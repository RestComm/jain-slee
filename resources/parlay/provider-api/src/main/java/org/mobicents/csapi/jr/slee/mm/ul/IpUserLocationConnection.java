package org.mobicents.csapi.jr.slee.mm.ul;

/**
 * This interface is the 'service manager' interface for the User Location Service.  
The user location interface provides the management functions to the user location service. The application programmer can use this interface to obtain the geographical location of users.
This interface, or IpTriggeredUserLocation, shall be implemented by a User Location SCF as a minimum requirement.  
The locationReportReq() method, or the extendedLocationReportReq() method, or both the periodicLocationReportingStartReq() and periodicLocationReportingStop() methods shall be implemented as a minimum requirement, if this interface is implemented.
 *
 * 
 * 
 */
public interface IpUserLocationConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     Request of a report on the location for one or several users.
@return  assignmentId 
Specifies the assignment ID of the location-report request.
     *     @param users Specifies the user(s) for which the location shall be reported.

     */
    int locationReportReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Advanced request of report on the location for one or several users.
@return  assignmentId 
Specifies the assignment ID of the extended location-report request.
     *     @param users Specifies the user(s) for which the location shall be reported
    @param request Specifies among others the requested location type, accuracy, response time and priority.

     */
    int extendedLocationReportReq(org.csapi.TpAddress[] users,org.csapi.mm.TpLocationRequest request) throws org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Request of periodic reports on the location for one or several users.
@return  assignmentId 
Specifies the assignment ID of the periodic location-reporting request.
     *     @param users Specifies the user(s) for which the location shall be reported.
    @param request Specifies among others the requested location type, accuracy, response time and priority.
    @param reportingInterval Specifies the requested interval in seconds between the reports.

     */
    int periodicLocationReportingStartReq(org.csapi.TpAddress[] users,org.csapi.mm.TpLocationRequest request,int reportingInterval) throws org.csapi.TpCommonExceptions,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Termination of periodic reports on the location for one or several users.
     *     @param stopRequest Specifies how the assignment shall be stopped, i.e. if whole or just parts of the assignment should be stopped.

     */
    void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpUserLocationConnection

