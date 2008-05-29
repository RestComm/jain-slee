package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallErrorTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallErrorType value;

	public TpCallErrorTypeHolder ()
	{
	}
	public TpCallErrorTypeHolder (final TpCallErrorType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallErrorTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallErrorTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallErrorTypeHelper.write (out,value);
	}
}
