package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessage"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMessageHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMessage value;
	public IpMessageHolder()
	{
	}
	public IpMessageHolder (final IpMessage initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMessageHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMessageHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMessageHelper.write (_out,value);
	}
}
