package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUserStatusOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int statusReportReq(org.csapi.mm.us.IpAppUserStatus appStatus, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	int triggeredStatusReportingStartReq(org.csapi.mm.us.IpAppUserStatus appStatus, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	void triggeredStatusReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
