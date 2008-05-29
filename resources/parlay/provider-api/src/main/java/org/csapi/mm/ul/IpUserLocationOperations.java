package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUserLocationOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int locationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED;
	int extendedLocationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED;
	int periodicLocationReportingStartReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request, int reportingInterval) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED;
	void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
