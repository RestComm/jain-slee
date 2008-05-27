package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpAppUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUserLocationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocation[] locations);
	void locationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
	void extendedLocationReportRes(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations);
	void extendedLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
	void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations);
	void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
}
