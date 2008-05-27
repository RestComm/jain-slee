package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpAppUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUserLocationCamelHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUserLocationCamel value;
	public IpAppUserLocationCamelHolder()
	{
	}
	public IpAppUserLocationCamelHolder (final IpAppUserLocationCamel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUserLocationCamelHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUserLocationCamelHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUserLocationCamelHelper.write (_out,value);
	}
}
