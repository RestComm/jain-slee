package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpEntOpIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpEntOpIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpEntOpIDListHolder ()
	{
	}
	public TpEntOpIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpEntOpIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpEntOpIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpEntOpIDListHelper.write (out,value);
	}
}
