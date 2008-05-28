package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUserStatusSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserStatus[] value;

	public TpUserStatusSetHolder ()
	{
	}
	public TpUserStatusSetHolder (final org.csapi.mm.TpUserStatus[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserStatusSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserStatusSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserStatusSetHelper.write (out,value);
	}
}
