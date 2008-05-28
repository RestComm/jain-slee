package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyExpressionAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyExpressionActionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyExpressionAction value;
	public IpPolicyExpressionActionHolder()
	{
	}
	public IpPolicyExpressionActionHolder (final IpPolicyExpressionAction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyExpressionActionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyExpressionActionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyExpressionActionHelper.write (_out,value);
	}
}
