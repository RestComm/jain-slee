package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppDataSessionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppDataSession value;
	public IpAppDataSessionHolder()
	{
	}
	public IpAppDataSessionHolder (final IpAppDataSession initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppDataSessionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppDataSessionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppDataSessionHelper.write (_out,value);
	}
}
