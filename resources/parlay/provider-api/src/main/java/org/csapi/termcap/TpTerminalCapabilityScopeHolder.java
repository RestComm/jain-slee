package org.csapi.termcap;

/**
 *	Generated from IDL definition of struct "TpTerminalCapabilityScope"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScopeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.termcap.TpTerminalCapabilityScope value;

	public TpTerminalCapabilityScopeHolder ()
	{
	}
	public TpTerminalCapabilityScopeHolder(final org.csapi.termcap.TpTerminalCapabilityScope initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.termcap.TpTerminalCapabilityScopeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.termcap.TpTerminalCapabilityScopeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.termcap.TpTerminalCapabilityScopeHelper.write(_out, value);
	}
}
