package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpAdditionalCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpAdditionalCallEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAdditionalCallEventCriteria value;

	public TpAdditionalCallEventCriteriaHolder ()
	{
	}
	public TpAdditionalCallEventCriteriaHolder (final TpAdditionalCallEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAdditionalCallEventCriteriaHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAdditionalCallEventCriteriaHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAdditionalCallEventCriteriaHelper.write (out, value);
	}
}
