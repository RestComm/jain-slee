package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of union "TpCallAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAdditionalReportInfo value;

	public TpCallAdditionalReportInfoHolder ()
	{
	}
	public TpCallAdditionalReportInfoHolder (final TpCallAdditionalReportInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAdditionalReportInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAdditionalReportInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAdditionalReportInfoHelper.write (out, value);
	}
}
