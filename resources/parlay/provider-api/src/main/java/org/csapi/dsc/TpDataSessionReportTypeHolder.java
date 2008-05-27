package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionReportType value;

	public TpDataSessionReportTypeHolder ()
	{
	}
	public TpDataSessionReportTypeHolder (final TpDataSessionReportType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionReportTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionReportTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionReportTypeHelper.write (out,value);
	}
}
