package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionEventCriteria value;

	public TpDataSessionEventCriteriaHolder ()
	{
	}
	public TpDataSessionEventCriteriaHolder(final org.csapi.dsc.TpDataSessionEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionEventCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionEventCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionEventCriteriaHelper.write(_out, value);
	}
}
