package org.csapi.mm;

/**
 *	Generated from IDL definition of alias "TpUserLocationCamelSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationCamelSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpUserLocationCamel[] value;

	public TpUserLocationCamelSetHolder ()
	{
	}
	public TpUserLocationCamelSetHolder (final org.csapi.mm.TpUserLocationCamel[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserLocationCamelSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserLocationCamelSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserLocationCamelSetHelper.write (out,value);
	}
}
