package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyRule"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyRuleHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyRule value;
	public IpPolicyRuleHolder()
	{
	}
	public IpPolicyRuleHolder (final IpPolicyRule initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyRuleHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyRuleHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyRuleHelper.write (_out,value);
	}
}
