package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpAppUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUserStatusOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void statusReportRes(int assignmentId, org.csapi.mm.TpUserStatus[] status);
	void statusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
	void triggeredStatusReport(int assignmentId, org.csapi.mm.TpUserStatus status);
	void triggeredStatusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
}
