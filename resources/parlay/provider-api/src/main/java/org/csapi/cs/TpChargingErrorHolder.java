package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingError"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpChargingError value;

	public TpChargingErrorHolder ()
	{
	}
	public TpChargingErrorHolder (final TpChargingError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingErrorHelper.write (out,value);
	}
}
