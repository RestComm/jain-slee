package org.csapi.mm.ule;

/**
 *	Generated from IDL interface "IpUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUserLocationEmergencyOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int emergencyLocationReportReq(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation, org.csapi.mm.TpUserLocationEmergencyRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER;
	int subscribeEmergencyLocationReports(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	void unSubscribeEmergencyLocationReports(int assignmentId) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
