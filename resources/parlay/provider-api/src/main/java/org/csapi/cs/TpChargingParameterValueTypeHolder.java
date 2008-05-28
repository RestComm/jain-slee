package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingParameterValueType"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValueTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpChargingParameterValueType value;

	public TpChargingParameterValueTypeHolder ()
	{
	}
	public TpChargingParameterValueTypeHolder (final TpChargingParameterValueType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingParameterValueTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingParameterValueTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingParameterValueTypeHelper.write (out,value);
	}
}
