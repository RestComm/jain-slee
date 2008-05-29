package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpClientAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpClientAccessHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpClientAccess value;
	public IpClientAccessHolder()
	{
	}
	public IpClientAccessHolder (final IpClientAccess initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpClientAccessHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpClientAccessHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpClientAccessHelper.write (_out,value);
	}
}
