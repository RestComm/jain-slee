package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpDataSessionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpDataSession value;
	public IpDataSessionHolder()
	{
	}
	public IpDataSessionHolder (final IpDataSession initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpDataSessionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpDataSessionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpDataSessionHelper.write (_out,value);
	}
}
