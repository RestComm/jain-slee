package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEndedReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEndedReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallEndedReport value;

	public TpCallEndedReportHolder ()
	{
	}
	public TpCallEndedReportHolder(final org.csapi.cc.gccs.TpCallEndedReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallEndedReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallEndedReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallEndedReportHelper.write(_out, value);
	}
}
