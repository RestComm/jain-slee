package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFWExceptionType"
 *	@author JacORB IDL compiler 
 */

public final class TpFWExceptionTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFWExceptionType value;

	public TpFWExceptionTypeHolder ()
	{
	}
	public TpFWExceptionTypeHolder (final TpFWExceptionType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFWExceptionTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFWExceptionTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFWExceptionTypeHelper.write (out,value);
	}
}
