package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyEventAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyEventActionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyEventAction value;
	public IpPolicyEventActionHolder()
	{
	}
	public IpPolicyEventActionHolder (final IpPolicyEventAction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyEventActionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyEventActionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyEventActionHelper.write (_out,value);
	}
}
