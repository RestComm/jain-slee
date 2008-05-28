package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallInfoReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallInfoReport value;

	public TpCallInfoReportHolder ()
	{
	}
	public TpCallInfoReportHolder(final org.csapi.cc.TpCallInfoReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallInfoReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallInfoReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallInfoReportHelper.write(_out, value);
	}
}
