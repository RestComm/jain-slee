package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpSagIDList"
 *	@author JacORB IDL compiler 
 */

public final class TpSagIDListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpSagIDListHolder ()
	{
	}
	public TpSagIDListHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSagIDListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSagIDListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSagIDListHelper.write (out,value);
	}
}
