package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of union "TpCallAdditionalReportCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalReportCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAdditionalReportCriteria value;

	public TpCallAdditionalReportCriteriaHolder ()
	{
	}
	public TpCallAdditionalReportCriteriaHolder (final TpCallAdditionalReportCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAdditionalReportCriteriaHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAdditionalReportCriteriaHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAdditionalReportCriteriaHelper.write (out, value);
	}
}
