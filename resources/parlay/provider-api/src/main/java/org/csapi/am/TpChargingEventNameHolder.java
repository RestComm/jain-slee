package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpChargingEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpChargingEventName value;

	public TpChargingEventNameHolder ()
	{
	}
	public TpChargingEventNameHolder (final TpChargingEventName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingEventNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingEventNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingEventNameHelper.write (out,value);
	}
}
