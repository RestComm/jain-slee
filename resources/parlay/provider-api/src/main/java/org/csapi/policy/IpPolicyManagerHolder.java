package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyManager value;
	public IpPolicyManagerHolder()
	{
	}
	public IpPolicyManagerHolder (final IpPolicyManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyManagerHelper.write (_out,value);
	}
}
