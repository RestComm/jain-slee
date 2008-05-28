package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilityScopeType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScopeTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpTerminalCapabilityScopeType value;

	public TpTerminalCapabilityScopeTypeHolder ()
	{
	}
	public TpTerminalCapabilityScopeTypeHolder (final TpTerminalCapabilityScopeType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpTerminalCapabilityScopeTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpTerminalCapabilityScopeTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpTerminalCapabilityScopeTypeHelper.write (out,value);
	}
}
