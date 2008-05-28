package org.csapi;

/**
 *	Generated from IDL definition of alias "TpAddressSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAddress[] value;

	public TpAddressSetHolder ()
	{
	}
	public TpAddressSetHolder (final org.csapi.TpAddress[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddressSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddressSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddressSetHelper.write (out,value);
	}
}
