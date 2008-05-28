package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpAppExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppExtendedTerminalCapabilitiesOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void triggeredTerminalCapabilityReport(int assignmentID, org.csapi.TpAddress[] terminals, int criteria, org.csapi.termcap.TpTerminalCapabilities capabilities);
	void triggeredTerminalCapabilityReportErr(int assignmentId, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilitiesError cause);
}
