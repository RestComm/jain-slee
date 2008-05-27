package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpClientAppIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpClientAppIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpClientAppIDListHolder ()
	{
	}
	public TpClientAppIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpClientAppIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpClientAppIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpClientAppIDListHelper.write (out,value);
	}
}
