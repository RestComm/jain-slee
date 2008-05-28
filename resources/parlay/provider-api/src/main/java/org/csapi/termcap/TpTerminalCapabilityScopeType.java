package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilityScopeType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScopeType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED = 0;
	public static final TpTerminalCapabilityScopeType P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED = new TpTerminalCapabilityScopeType(_P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED);
	public static final int _P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP = 1;
	public static final TpTerminalCapabilityScopeType P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP = new TpTerminalCapabilityScopeType(_P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP);
	public int value()
	{
		return value;
	}
	public static TpTerminalCapabilityScopeType from_int(int value)
	{
		switch (value) {
			case _P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED: return P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED;
			case _P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP: return P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTerminalCapabilityScopeType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
