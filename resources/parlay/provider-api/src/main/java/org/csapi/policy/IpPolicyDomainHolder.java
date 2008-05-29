package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyDomainHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyDomain value;
	public IpPolicyDomainHolder()
	{
	}
	public IpPolicyDomainHolder (final IpPolicyDomain initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyDomainHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyDomainHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyDomainHelper.write (_out,value);
	}
}
