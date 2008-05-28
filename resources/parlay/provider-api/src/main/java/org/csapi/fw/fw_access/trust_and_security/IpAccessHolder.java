package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAccessHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAccess value;
	public IpAccessHolder()
	{
	}
	public IpAccessHolder (final IpAccess initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAccessHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAccessHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAccessHelper.write (_out,value);
	}
}
