package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIError"
 *	@author JacORB IDL compiler 
 */

public final class TpUIErrorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIError value;

	public TpUIErrorHolder ()
	{
	}
	public TpUIErrorHolder (final TpUIError initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIErrorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIErrorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIErrorHelper.write (out,value);
	}
}
