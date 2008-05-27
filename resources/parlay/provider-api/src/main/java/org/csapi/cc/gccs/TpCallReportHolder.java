package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallReport value;

	public TpCallReportHolder ()
	{
	}
	public TpCallReportHolder(final org.csapi.cc.gccs.TpCallReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallReportHelper.write(_out, value);
	}
}
