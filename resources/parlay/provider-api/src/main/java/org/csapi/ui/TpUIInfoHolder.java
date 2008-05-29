package org.csapi.ui;
/**
 *	Generated from IDL definition of union "TpUIInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUIInfo value;

	public TpUIInfoHolder ()
	{
	}
	public TpUIInfoHolder (final TpUIInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIInfoHelper.write (out, value);
	}
}
