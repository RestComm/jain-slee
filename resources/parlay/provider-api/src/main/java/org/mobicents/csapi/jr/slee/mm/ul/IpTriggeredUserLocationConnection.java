package org.mobicents.csapi.jr.slee.mm.ul;

/**
 * This interface can be used as an extended version of the User Location: Service Interface.  
The triggered user location interface represents the interface to the triggered user location functions. The application programmer can use this interface to request user location reports that are triggered by location change.
This interface, or IpUserLocation, shall be implemented by a User Location SCF as a minimum requirement.
The triggeredLocationReportingStartReq() and triggeredLocationReportingStop() methods shall be implemented as a minimum requirement. An implementation of IpTriggeredUserLocation is not required to implement the minimum mandatory methods of IpUserLocation.
 *
 * 
 * 
 */
public interface IpTriggeredUserLocationConnection extends org.mobicents.csapi.jr.slee.mm.ul.IpUserLocationConnection{


    /**
     *     Request for user location reports when the location is changed (reports are triggered by location change).
@return  assignmentId 
Specifies the assignment ID of the triggered location-reporting request.
     *     @param users Specifies the user(s) for which the location shall be reported.
    @param request Specifies among others the requested location type, accuracy, response time and priority.
    @param triggers Specifies the trigger conditions.

     */
    int triggeredLocationReportingStartReq(org.csapi.TpAddress[] users,org.csapi.mm.TpLocationRequest request,org.csapi.mm.TpLocationTrigger[] triggers) throws org.csapi.TpCommonExceptions,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_INFORMATION_NOT_AVAILABLE,javax.slee.resource.ResourceException;


    /**
     *     Stop triggered user location reporting.
     *     @param stopRequest Specifies how the assignment shall be stopped, i.e. if whole or just parts of the assignment should be stopped.

     */
    void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpTriggeredUserLocationConnection

