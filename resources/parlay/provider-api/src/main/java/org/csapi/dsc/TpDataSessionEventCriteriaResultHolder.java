package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaResultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionEventCriteriaResult value;

	public TpDataSessionEventCriteriaResultHolder ()
	{
	}
	public TpDataSessionEventCriteriaResultHolder(final org.csapi.dsc.TpDataSessionEventCriteriaResult initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionEventCriteriaResultHelper.write(_out, value);
	}
}
