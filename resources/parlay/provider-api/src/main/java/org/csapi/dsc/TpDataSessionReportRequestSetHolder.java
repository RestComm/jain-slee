package org.csapi.dsc;

/**
 *	Generated from IDL definition of alias "TpDataSessionReportRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportRequestSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionReportRequest[] value;

	public TpDataSessionReportRequestSetHolder ()
	{
	}
	public TpDataSessionReportRequestSetHolder (final org.csapi.dsc.TpDataSessionReportRequest[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionReportRequestSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionReportRequestSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionReportRequestSetHelper.write (out,value);
	}
}
