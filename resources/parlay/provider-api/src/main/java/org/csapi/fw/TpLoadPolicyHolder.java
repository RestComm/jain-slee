package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadPolicyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpLoadPolicy value;

	public TpLoadPolicyHolder ()
	{
	}
	public TpLoadPolicyHolder(final org.csapi.fw.TpLoadPolicy initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpLoadPolicyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpLoadPolicyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpLoadPolicyHelper.write(_out, value);
	}
}
