package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpIPv4AddType"
 *	@author JacORB IDL compiler 
 */

public final class TpIPv4AddTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpIPv4AddType value;

	public TpIPv4AddTypeHolder ()
	{
	}
	public TpIPv4AddTypeHolder (final TpIPv4AddType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpIPv4AddTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpIPv4AddTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpIPv4AddTypeHelper.write (out,value);
	}
}
