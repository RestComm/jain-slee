package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpChargingSessionID"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingSessionIDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpChargingSessionID value;

	public TpChargingSessionIDHolder ()
	{
	}
	public TpChargingSessionIDHolder(final org.csapi.cs.TpChargingSessionID initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpChargingSessionIDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpChargingSessionIDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpChargingSessionIDHelper.write(_out, value);
	}
}
