package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpAppUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUserStatusHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppUserStatus value;
	public IpAppUserStatusHolder()
	{
	}
	public IpAppUserStatusHolder (final IpAppUserStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppUserStatusHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppUserStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppUserStatusHelper.write (_out,value);
	}
}
