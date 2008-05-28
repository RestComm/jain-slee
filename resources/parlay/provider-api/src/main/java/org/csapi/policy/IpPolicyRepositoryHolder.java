package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyRepository"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyRepositoryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyRepository value;
	public IpPolicyRepositoryHolder()
	{
	}
	public IpPolicyRepositoryHolder (final IpPolicyRepository initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyRepositoryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyRepositoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyRepositoryHelper.write (_out,value);
	}
}
