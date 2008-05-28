package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionMonitorMode"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionMonitorModeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionMonitorMode value;

	public TpDataSessionMonitorModeHolder ()
	{
	}
	public TpDataSessionMonitorModeHolder (final TpDataSessionMonitorMode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionMonitorModeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionMonitorModeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionMonitorModeHelper.write (out,value);
	}
}
