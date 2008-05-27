package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyEventDefinition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyEventDefinitionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyEventDefinition value;
	public IpPolicyEventDefinitionHolder()
	{
	}
	public IpPolicyEventDefinitionHolder (final IpPolicyEventDefinition initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyEventDefinitionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyEventDefinitionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyEventDefinitionHelper.write (_out,value);
	}
}
