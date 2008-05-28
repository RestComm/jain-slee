package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUserLocationCamelHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUserLocationCamel value;
	public IpUserLocationCamelHolder()
	{
	}
	public IpUserLocationCamelHolder (final IpUserLocationCamel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUserLocationCamelHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUserLocationCamelHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUserLocationCamelHelper.write (_out,value);
	}
}
