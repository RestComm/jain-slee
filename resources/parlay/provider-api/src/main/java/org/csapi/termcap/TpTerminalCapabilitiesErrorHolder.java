package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilitiesError"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilitiesErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTerminalCapabilitiesError value;

	public TpTerminalCapabilitiesErrorHolder ()
	{
	}
	public TpTerminalCapabilitiesErrorHolder (final TpTerminalCapabilitiesError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTerminalCapabilitiesErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTerminalCapabilitiesErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTerminalCapabilitiesErrorHelper.write (out,value);
	}
}
