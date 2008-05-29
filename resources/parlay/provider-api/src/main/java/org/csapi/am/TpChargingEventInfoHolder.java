package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpChargingEventInfo value;

	public TpChargingEventInfoHolder ()
	{
	}
	public TpChargingEventInfoHolder(final org.csapi.am.TpChargingEventInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpChargingEventInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpChargingEventInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpChargingEventInfoHelper.write(_out, value);
	}
}
