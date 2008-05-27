package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallReportRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallReportRequest value;

	public TpCallReportRequestHolder ()
	{
	}
	public TpCallReportRequestHolder(final org.csapi.cc.gccs.TpCallReportRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallReportRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallReportRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallReportRequestHelper.write(_out, value);
	}
}
