package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaResultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallEventCriteriaResult value;

	public TpCallEventCriteriaResultHolder ()
	{
	}
	public TpCallEventCriteriaResultHolder(final org.csapi.cc.gccs.TpCallEventCriteriaResult initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallEventCriteriaResultHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallEventCriteriaResultHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallEventCriteriaResultHelper.write(_out, value);
	}
}
