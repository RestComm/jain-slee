package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpLoadStatisticList"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadStatistic[] value;

	public TpLoadStatisticListHolder ()
	{
	}
	public TpLoadStatisticListHolder (final org.csapi.fw.TpLoadStatistic[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatisticListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatisticListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatisticListHelper.write (out,value);
	}
}
