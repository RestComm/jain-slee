package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargePlanHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionChargePlan value;

	public TpDataSessionChargePlanHolder ()
	{
	}
	public TpDataSessionChargePlanHolder(final org.csapi.dsc.TpDataSessionChargePlan initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.dsc.TpDataSessionChargePlanHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.dsc.TpDataSessionChargePlanHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.dsc.TpDataSessionChargePlanHelper.write(_out, value);
	}
}
