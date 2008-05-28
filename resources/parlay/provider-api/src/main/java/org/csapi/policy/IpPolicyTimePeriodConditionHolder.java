package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyTimePeriodCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyTimePeriodConditionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyTimePeriodCondition value;
	public IpPolicyTimePeriodConditionHolder()
	{
	}
	public IpPolicyTimePeriodConditionHolder (final IpPolicyTimePeriodCondition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyTimePeriodConditionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyTimePeriodConditionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyTimePeriodConditionHelper.write (_out,value);
	}
}
