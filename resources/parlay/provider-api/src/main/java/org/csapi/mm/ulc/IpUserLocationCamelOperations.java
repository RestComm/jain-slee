package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUserLocationCamelOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int locationReportReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	int periodicLocationReportingStartReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users, int reportingInterval) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.P_UNKNOWN_SUBSCRIBER;
	void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	int triggeredLocationReportingStartReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationTriggerCamel trigger) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
