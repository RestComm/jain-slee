package org.csapi;

/**
 *	Generated from IDL interface "IpInterface"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpInterfaceHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpInterface value;
	public IpInterfaceHolder()
	{
	}
	public IpInterfaceHolder (final IpInterface initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpInterfaceHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpInterfaceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpInterfaceHelper.write (_out,value);
	}
}
