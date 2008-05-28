package org.csapi.dsc;
/**
 *	Generated from IDL definition of union "TpDataSessionAdditionalReportInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionAdditionalReportInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionAdditionalReportInfo value;

	public TpDataSessionAdditionalReportInfoHolder ()
	{
	}
	public TpDataSessionAdditionalReportInfoHolder (final TpDataSessionAdditionalReportInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionAdditionalReportInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionAdditionalReportInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionAdditionalReportInfoHelper.write (out, value);
	}
}
