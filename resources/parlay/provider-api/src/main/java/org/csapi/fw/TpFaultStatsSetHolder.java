package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpFaultStatsSet"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpFaultStats[] value;

	public TpFaultStatsSetHolder ()
	{
	}
	public TpFaultStatsSetHolder (final org.csapi.fw.TpFaultStats[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFaultStatsSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFaultStatsSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFaultStatsSetHelper.write (out,value);
	}
}
