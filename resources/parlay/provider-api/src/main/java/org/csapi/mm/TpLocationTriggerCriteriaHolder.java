package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationTriggerCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLocationTriggerCriteria value;

	public TpLocationTriggerCriteriaHolder ()
	{
	}
	public TpLocationTriggerCriteriaHolder (final TpLocationTriggerCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLocationTriggerCriteriaHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLocationTriggerCriteriaHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLocationTriggerCriteriaHelper.write (out,value);
	}
}
