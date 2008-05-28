package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIEventCriteria value;

	public TpUIEventCriteriaHolder ()
	{
	}
	public TpUIEventCriteriaHolder(final org.csapi.ui.TpUIEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIEventCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIEventCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIEventCriteriaHelper.write(_out, value);
	}
}
