package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionReport"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionReport value;

	public TpDataSessionReportHolder ()
	{
	}
	public TpDataSessionReportHolder(final org.csapi.dsc.TpDataSessionReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionReportHelper.write(_out, value);
	}
}
