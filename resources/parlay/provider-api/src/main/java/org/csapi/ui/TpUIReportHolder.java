package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIReport"
 *	@author JacORB IDL compiler 
 */

public final class TpUIReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIReport value;

	public TpUIReportHolder ()
	{
	}
	public TpUIReportHolder (final TpUIReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIReportHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIReportHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIReportHelper.write (out,value);
	}
}
