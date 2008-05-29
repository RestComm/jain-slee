package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionErrorTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpDataSessionErrorType value;

	public TpDataSessionErrorTypeHolder ()
	{
	}
	public TpDataSessionErrorTypeHolder (final TpDataSessionErrorType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionErrorTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionErrorTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionErrorTypeHelper.write (out,value);
	}
}
