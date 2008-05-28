package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyGroup"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyGroupHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyGroup value;
	public IpPolicyGroupHolder()
	{
	}
	public IpPolicyGroupHolder (final IpPolicyGroup initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyGroupHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyGroupHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyGroupHelper.write (_out,value);
	}
}
