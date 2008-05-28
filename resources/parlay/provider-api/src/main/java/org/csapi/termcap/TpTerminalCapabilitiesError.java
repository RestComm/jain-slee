package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilitiesError"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilitiesError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_TERMCAP_ERROR_UNDEFINED = 0;
	public static final TpTerminalCapabilitiesError P_TERMCAP_ERROR_UNDEFINED = new TpTerminalCapabilitiesError(_P_TERMCAP_ERROR_UNDEFINED);
	public static final int _P_TERMCAP_INVALID_TERMINALID = 1;
	public static final TpTerminalCapabilitiesError P_TERMCAP_INVALID_TERMINALID = new TpTerminalCapabilitiesError(_P_TERMCAP_INVALID_TERMINALID);
	public static final int _P_TERMCAP_SYSTEM_FAILURE = 2;
	public static final TpTerminalCapabilitiesError P_TERMCAP_SYSTEM_FAILURE = new TpTerminalCapabilitiesError(_P_TERMCAP_SYSTEM_FAILURE);
	public static final int _P_TERMCAP_INFO_UNAVAILABLE = 3;
	public static final TpTerminalCapabilitiesError P_TERMCAP_INFO_UNAVAILABLE = new TpTerminalCapabilitiesError(_P_TERMCAP_INFO_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpTerminalCapabilitiesError from_int(int value)
	{
		switch (value) {
			case _P_TERMCAP_ERROR_UNDEFINED: return P_TERMCAP_ERROR_UNDEFINED;
			case _P_TERMCAP_INVALID_TERMINALID: return P_TERMCAP_INVALID_TERMINALID;
			case _P_TERMCAP_SYSTEM_FAILURE: return P_TERMCAP_SYSTEM_FAILURE;
			case _P_TERMCAP_INFO_UNAVAILABLE: return P_TERMCAP_INFO_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTerminalCapabilitiesError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
