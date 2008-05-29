package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyActionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyAction value;
	public IpPolicyActionHolder()
	{
	}
	public IpPolicyActionHolder (final IpPolicyAction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyActionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyActionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyActionHelper.write (_out,value);
	}
}
