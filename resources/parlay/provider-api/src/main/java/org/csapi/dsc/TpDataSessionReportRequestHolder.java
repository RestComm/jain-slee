package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReportRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportRequestHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionReportRequest value;

	public TpDataSessionReportRequestHolder ()
	{
	}
	public TpDataSessionReportRequestHolder(final org.csapi.dsc.TpDataSessionReportRequest initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionReportRequestHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionReportRequestHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionReportRequestHelper.write(_out, value);
	}
}
