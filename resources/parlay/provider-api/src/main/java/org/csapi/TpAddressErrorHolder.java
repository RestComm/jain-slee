package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressError"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAddressError value;

	public TpAddressErrorHolder ()
	{
	}
	public TpAddressErrorHolder (final TpAddressError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddressErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddressErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddressErrorHelper.write (out,value);
	}
}
