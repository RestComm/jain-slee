package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUIHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUI value;
	public IpUIHolder()
	{
	}
	public IpUIHolder (final IpUI initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUIHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUIHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUIHelper.write (_out,value);
	}
}
