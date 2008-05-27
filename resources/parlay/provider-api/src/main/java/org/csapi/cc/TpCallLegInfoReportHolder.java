package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallLegInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegInfoReportHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallLegInfoReport value;

	public TpCallLegInfoReportHolder ()
	{
	}
	public TpCallLegInfoReportHolder(final org.csapi.cc.TpCallLegInfoReport initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallLegInfoReportHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallLegInfoReportHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallLegInfoReportHelper.write(_out, value);
	}
}
