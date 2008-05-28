package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyExpressionCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyExpressionConditionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyExpressionCondition value;
	public IpPolicyExpressionConditionHolder()
	{
	}
	public IpPolicyExpressionConditionHolder (final IpPolicyExpressionCondition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyExpressionConditionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyExpressionConditionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyExpressionConditionHelper.write (_out,value);
	}
}
