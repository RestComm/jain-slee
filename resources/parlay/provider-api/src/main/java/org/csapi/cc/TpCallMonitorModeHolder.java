package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallMonitorMode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallMonitorModeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallMonitorMode value;

	public TpCallMonitorModeHolder ()
	{
	}
	public TpCallMonitorModeHolder (final TpCallMonitorMode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallMonitorModeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallMonitorModeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallMonitorModeHelper.write (out,value);
	}
}
