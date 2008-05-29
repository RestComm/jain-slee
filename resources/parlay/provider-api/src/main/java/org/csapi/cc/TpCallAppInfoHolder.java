package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallAppInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAppInfo value;

	public TpCallAppInfoHolder ()
	{
	}
	public TpCallAppInfoHolder (final TpCallAppInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAppInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAppInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAppInfoHelper.write (out, value);
	}
}
