package org.mobicents.csapi.jr.slee.mm.us;

/**
 * The application programmer can use this interface to obtain the status of fixed, mobile and IP-based telephony users.
This interface shall be implemented by a User Status SCF.
The statusReportReq() method, or both the triggeredStatusReportingStartReq() and trigggeredStatusReportingStop() methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpUserStatusConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     Request for a report on the status of one or several users.
@return  assignmentId 
Specifies the assignment ID of the status-report request.
     *     @param users Specifies the user(s) for which the status shall be reported.

     */
    int statusReportReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.P_APPLICATION_NOT_ACTIVATED,javax.slee.resource.ResourceException;


    /**
     *     Request for triggered status reports when one or several user's status is changed. The user status service will send a report when the status changes.
@return  assignmentId 
Specifies the assignment ID of the triggered status-reporting request.
     *     @param users Specifies the user(s) for which the status changes shall be reported.

     */
    int triggeredStatusReportingStartReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.P_APPLICATION_NOT_ACTIVATED,javax.slee.resource.ResourceException;


    /**
     *     This method stops the sending of status reports for one or several users.
     *     @param stopRequest Specifies how the assignment shall be stopped, i.e. if whole or just parts of the assignment should be stopped.

     */
    void triggeredStatusReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpUserStatusConnection

