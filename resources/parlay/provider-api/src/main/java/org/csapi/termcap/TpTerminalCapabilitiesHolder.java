package org.csapi.termcap;

/**
 *	Generated from IDL definition of struct "TpTerminalCapabilities"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilitiesHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.termcap.TpTerminalCapabilities value;

	public TpTerminalCapabilitiesHolder ()
	{
	}
	public TpTerminalCapabilitiesHolder(final org.csapi.termcap.TpTerminalCapabilities initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.termcap.TpTerminalCapabilitiesHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.termcap.TpTerminalCapabilitiesHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.termcap.TpTerminalCapabilitiesHelper.write(_out, value);
	}
}
