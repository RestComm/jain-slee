package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallReportType value;

	public TpCallReportTypeHolder ()
	{
	}
	public TpCallReportTypeHolder (final TpCallReportType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallReportTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallReportTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallReportTypeHelper.write (out,value);
	}
}
