package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpFaultStatsErrorList"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsErrorListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpFaultStatisticsError[] value;

	public TpFaultStatsErrorListHolder ()
	{
	}
	public TpFaultStatsErrorListHolder (final org.csapi.fw.TpFaultStatisticsError[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFaultStatsErrorListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFaultStatsErrorListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFaultStatsErrorListHelper.write (out,value);
	}
}
