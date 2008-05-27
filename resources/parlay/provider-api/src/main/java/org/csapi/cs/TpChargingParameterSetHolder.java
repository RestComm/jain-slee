package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpChargingParameterSet"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpChargingParameter[] value;

	public TpChargingParameterSetHolder ()
	{
	}
	public TpChargingParameterSetHolder (final org.csapi.cs.TpChargingParameter[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingParameterSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingParameterSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingParameterSetHelper.write (out,value);
	}
}
