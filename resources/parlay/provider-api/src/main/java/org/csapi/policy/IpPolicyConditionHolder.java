package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyConditionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyCondition value;
	public IpPolicyConditionHolder()
	{
	}
	public IpPolicyConditionHolder (final IpPolicyCondition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyConditionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyConditionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyConditionHelper.write (_out,value);
	}
}
