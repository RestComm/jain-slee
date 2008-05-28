package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAddress"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAddress value;

	public TpAddressHolder ()
	{
	}
	public TpAddressHolder(final org.csapi.TpAddress initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpAddressHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpAddressHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpAddressHelper.write(_out, value);
	}
}
