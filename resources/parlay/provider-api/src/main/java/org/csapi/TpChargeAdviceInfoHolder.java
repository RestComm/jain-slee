package org.csapi;

/**
 *	Generated from IDL definition of struct "TpChargeAdviceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargeAdviceInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpChargeAdviceInfo value;

	public TpChargeAdviceInfoHolder ()
	{
	}
	public TpChargeAdviceInfoHolder(final org.csapi.TpChargeAdviceInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpChargeAdviceInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpChargeAdviceInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpChargeAdviceInfoHelper.write(_out, value);
	}
}
