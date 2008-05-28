package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIMessageCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIMessageCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIMessageCriteria value;

	public TpUIMessageCriteriaHolder ()
	{
	}
	public TpUIMessageCriteriaHolder(final org.csapi.ui.TpUIMessageCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIMessageCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIMessageCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIMessageCriteriaHelper.write(_out, value);
	}
}
