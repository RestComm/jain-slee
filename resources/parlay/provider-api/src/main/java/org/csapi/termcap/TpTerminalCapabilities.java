package org.csapi.termcap;

/**
 *	Generated from IDL definition of struct "TpTerminalCapabilities"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilities
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpTerminalCapabilities(){}
	public java.lang.String TerminalCapabilities;
	public boolean StatusCode;
	public TpTerminalCapabilities(java.lang.String TerminalCapabilities, boolean StatusCode)
	{
		this.TerminalCapabilities = TerminalCapabilities;
		this.StatusCode = StatusCode;
	}
}
