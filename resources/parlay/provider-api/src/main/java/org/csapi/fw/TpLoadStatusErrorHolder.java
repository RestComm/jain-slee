package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatusError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatusErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpLoadStatusError value;

	public TpLoadStatusErrorHolder ()
	{
	}
	public TpLoadStatusErrorHolder (final TpLoadStatusError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpLoadStatusErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpLoadStatusErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpLoadStatusErrorHelper.write (out,value);
	}
}
