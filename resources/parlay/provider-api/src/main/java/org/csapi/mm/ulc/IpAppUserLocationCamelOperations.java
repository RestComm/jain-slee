package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpAppUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUserLocationCamelOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations);
	void locationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
	void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations);
	void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
	void triggeredLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel location, org.csapi.mm.TpLocationTriggerCamel criterion);
	void triggeredLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
}
