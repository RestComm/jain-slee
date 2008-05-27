package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of alias "TpCallReportRequestSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportRequestSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallReportRequest[] value;

	public TpCallReportRequestSetHolder ()
	{
	}
	public TpCallReportRequestSetHolder (final org.csapi.cc.gccs.TpCallReportRequest[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallReportRequestSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallReportRequestSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallReportRequestSetHelper.write (out,value);
	}
}
