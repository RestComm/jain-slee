package org.csapi;

/**
 *	Generated from IDL definition of alias "TpStringSet"
 *	@author JacORB IDL compiler 
 */

public final class TpStringSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpStringSetHolder ()
	{
	}
	public TpStringSetHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpStringSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpStringSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpStringSetHelper.write (out,value);
	}
}
