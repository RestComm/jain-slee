package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUserStatusHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpUserStatus value;
	public IpUserStatusHolder()
	{
	}
	public IpUserStatusHolder (final IpUserStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpUserStatusHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpUserStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpUserStatusHelper.write (_out,value);
	}
}
