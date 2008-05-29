package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpTriggeredUserLocationOperations
	extends org.csapi.mm.ul.IpUserLocationOperations
{
	/* constants */
	/* operations  */
	int triggeredLocationReportingStartReq(org.csapi.mm.ul.IpAppTriggeredUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request, org.csapi.mm.TpLocationTrigger[] triggers) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
