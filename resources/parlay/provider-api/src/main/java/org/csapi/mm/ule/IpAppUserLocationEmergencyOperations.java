package org.csapi.mm.ule;

/**
 *	Generated from IDL interface "IpAppUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUserLocationEmergencyOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void emergencyLocationReport(int assignmentId, org.csapi.mm.TpUserLocationEmergency location);
	void emergencyLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
}
