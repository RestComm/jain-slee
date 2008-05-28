package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUICollectCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUICollectCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUICollectCriteria value;

	public TpUICollectCriteriaHolder ()
	{
	}
	public TpUICollectCriteriaHolder(final org.csapi.ui.TpUICollectCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUICollectCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUICollectCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUICollectCriteriaHelper.write(_out, value);
	}
}
