package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAuthenticationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAuthentication value;
	public IpAuthenticationHolder()
	{
	}
	public IpAuthenticationHolder (final IpAuthentication initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAuthenticationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAuthenticationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAuthenticationHelper.write (_out,value);
	}
}
