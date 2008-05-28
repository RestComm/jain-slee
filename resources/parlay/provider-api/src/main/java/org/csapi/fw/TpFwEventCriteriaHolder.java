package org.csapi.fw;
/**
 *	Generated from IDL definition of union "TpFwEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFwEventCriteria value;

	public TpFwEventCriteriaHolder ()
	{
	}
	public TpFwEventCriteriaHolder (final TpFwEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFwEventCriteriaHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFwEventCriteriaHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFwEventCriteriaHelper.write (out, value);
	}
}
