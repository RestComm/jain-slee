package org.csapi;

/**
 *	Generated from IDL definition of alias "TpSessionIDSet"
 *	@author JacORB IDL compiler 
 */

public final class TpSessionIDSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public int[] value;

	public TpSessionIDSetHolder ()
	{
	}
	public TpSessionIDSetHolder (final int[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSessionIDSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSessionIDSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSessionIDSetHelper.write (out,value);
	}
}
