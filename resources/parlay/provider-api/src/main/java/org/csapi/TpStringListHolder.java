package org.csapi;

/**
 *	Generated from IDL definition of alias "TpStringList"
 *	@author JacORB IDL compiler 
 */

public final class TpStringListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpStringListHolder ()
	{
	}
	public TpStringListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpStringListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpStringListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpStringListHelper.write (out,value);
	}
}
