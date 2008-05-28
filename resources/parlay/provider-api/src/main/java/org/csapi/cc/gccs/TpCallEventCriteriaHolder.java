package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallEventCriteria value;

	public TpCallEventCriteriaHolder ()
	{
	}
	public TpCallEventCriteriaHolder(final org.csapi.cc.gccs.TpCallEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallEventCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallEventCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallEventCriteriaHelper.write(_out, value);
	}
}
