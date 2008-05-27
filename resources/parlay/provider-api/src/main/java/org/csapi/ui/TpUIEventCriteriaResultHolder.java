package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaResultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIEventCriteriaResult value;

	public TpUIEventCriteriaResultHolder ()
	{
	}
	public TpUIEventCriteriaResultHolder(final org.csapi.ui.TpUIEventCriteriaResult initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.TpUIEventCriteriaResultHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.TpUIEventCriteriaResultHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.TpUIEventCriteriaResultHelper.write(_out, value);
	}
}
