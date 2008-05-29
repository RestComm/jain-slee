package org.csapi;

/**
 *	Generated from IDL definition of struct "TpChargePerTime"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerTimeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpChargePerTime value;

	public TpChargePerTimeHolder ()
	{
	}
	public TpChargePerTimeHolder(final org.csapi.TpChargePerTime initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpChargePerTimeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpChargePerTimeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpChargePerTimeHelper.write(_out, value);
	}
}
