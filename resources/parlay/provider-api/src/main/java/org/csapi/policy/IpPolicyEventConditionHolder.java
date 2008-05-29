package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyEventCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyEventConditionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyEventCondition value;
	public IpPolicyEventConditionHolder()
	{
	}
	public IpPolicyEventConditionHolder (final IpPolicyEventCondition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyEventConditionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyEventConditionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyEventConditionHelper.write (_out,value);
	}
}
