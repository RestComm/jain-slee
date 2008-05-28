package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFaultStatisticsError"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatisticsErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFaultStatisticsError value;

	public TpFaultStatisticsErrorHolder ()
	{
	}
	public TpFaultStatisticsErrorHolder (final TpFaultStatisticsError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFaultStatisticsErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFaultStatisticsErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFaultStatisticsErrorHelper.write (out,value);
	}
}
