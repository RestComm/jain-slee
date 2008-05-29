package org.mobicents.csapi.jr.slee.mm.ule;

/**
 * The application programmer can use this interface to obtain the location of users who initiate emergency calls.
This interface shall be implemented by a Mobility SCF.
The emergencyLocationReportReq() method, or the subscribeEmergencyLocationReports() and unSubscribeEmergencyLocationReports() methods shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpUserLocationEmergencyConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     Request of report on the location for one user that is making an emergency call.
@return  assignmentId 
Specifies the assignment ID of the emergency location-report request.
     *     @param request Specifies among others the identity of the user or terminal, requested location type, accuracy, response time and priority.

     */
    int emergencyLocationReportReq(org.csapi.mm.TpUserLocationEmergencyRequest request) throws org.csapi.TpCommonExceptions,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.P_APPLICATION_NOT_ACTIVATED,javax.slee.resource.ResourceException;


    /**
     *     Subscribe to network initiated emergency user location reports.
@return  assignmentId 
Specifies the assignment ID of the subscription.
     * 
     */
    int subscribeEmergencyLocationReports() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method cancels a subscription to network initiated emergency user location reports.
The assignment ID does not correspond to one of a valid assignment.
     *     @param assignmentId Specifies the assignment ID of the subscription.

     */
    void unSubscribeEmergencyLocationReports(int assignmentId) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpUserLocationEmergencyConnection

