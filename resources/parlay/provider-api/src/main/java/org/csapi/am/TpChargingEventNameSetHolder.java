package org.csapi.am;

/**
 *	Generated from IDL definition of alias "TpChargingEventNameSet"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventNameSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpChargingEventName[] value;

	public TpChargingEventNameSetHolder ()
	{
	}
	public TpChargingEventNameSetHolder (final org.csapi.am.TpChargingEventName[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingEventNameSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingEventNameSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingEventNameSetHelper.write (out,value);
	}
}
