package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpAppTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppTriggeredUserLocationOperations
	extends org.csapi.mm.ul.IpAppUserLocationOperations
{
	/* constants */
	/* operations  */
	void triggeredLocationReport(int assignmentId, org.csapi.mm.TpUserLocationExtended location, org.csapi.mm.TpLocationTriggerCriteria criterion);
	void triggeredLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic);
}
