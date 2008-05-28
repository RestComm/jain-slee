package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAPILevelAuthenticationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAPILevelAuthentication value;
	public IpAPILevelAuthenticationHolder()
	{
	}
	public IpAPILevelAuthenticationHolder (final IpAPILevelAuthentication initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAPILevelAuthenticationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAPILevelAuthenticationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAPILevelAuthenticationHelper.write (_out,value);
	}
}
