package org.csapi.cs;
/**
 *	Generated from IDL definition of union "TpChargingParameterValue"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpChargingParameterValue value;

	public TpChargingParameterValueHolder ()
	{
	}
	public TpChargingParameterValueHolder (final TpChargingParameterValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingParameterValueHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingParameterValueHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingParameterValueHelper.write (out, value);
	}
}
