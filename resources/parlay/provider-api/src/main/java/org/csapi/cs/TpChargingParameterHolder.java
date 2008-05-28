package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpChargingParameter"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cs.TpChargingParameter value;

	public TpChargingParameterHolder ()
	{
	}
	public TpChargingParameterHolder(final org.csapi.cs.TpChargingParameter initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cs.TpChargingParameterHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cs.TpChargingParameterHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cs.TpChargingParameterHelper.write(_out, value);
	}
}
