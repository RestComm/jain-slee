package org.csapi.policy;

/**
 *	Generated from IDL interface "IpAppPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppPolicyDomainHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppPolicyDomain value;
	public IpAppPolicyDomainHolder()
	{
	}
	public IpAppPolicyDomainHolder (final IpAppPolicyDomain initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppPolicyDomainHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppPolicyDomainHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppPolicyDomainHelper.write (_out,value);
	}
}
