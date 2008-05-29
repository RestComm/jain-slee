package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpTerminalCapabilitiesOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.termcap.TpTerminalCapabilities getTerminalCapabilities(java.lang.String terminalIdentity) throws org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID;
}
