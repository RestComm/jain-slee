package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallInfoReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallInfoReport value;

	public TpCallInfoReportHolder ()
	{
	}
	public TpCallInfoReportHolder(final org.csapi.cc.gccs.TpCallInfoReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallInfoReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallInfoReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallInfoReportHelper.write(_out, value);
	}
}
