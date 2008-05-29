package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargePlanHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallChargePlan value;

	public TpCallChargePlanHolder ()
	{
	}
	public TpCallChargePlanHolder(final org.csapi.cc.TpCallChargePlan initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallChargePlanHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallChargePlanHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallChargePlanHelper.write(_out, value);
	}
}
