package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpExtendedTerminalCapabilitiesOperations
	extends org.csapi.termcap.IpTerminalCapabilitiesOperations
{
	/* constants */
	/* operations  */
	int triggeredTerminalCapabilityStartReq(org.csapi.termcap.IpAppExtendedTerminalCapabilities appTerminalCapabilities, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilityScope capabilityScope, int criteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID,org.csapi.P_INVALID_CRITERIA;
	void triggeredTerminalCapabilityStop(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
