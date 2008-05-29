package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpClientAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpClientAPILevelAuthenticationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpClientAPILevelAuthentication value;
	public IpClientAPILevelAuthenticationHolder()
	{
	}
	public IpClientAPILevelAuthenticationHolder (final IpClientAPILevelAuthentication initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpClientAPILevelAuthenticationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpClientAPILevelAuthenticationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpClientAPILevelAuthenticationHelper.write (_out,value);
	}
}
