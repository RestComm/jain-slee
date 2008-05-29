package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicy"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicy value;
	public IpPolicyHolder()
	{
	}
	public IpPolicyHolder (final IpPolicy initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyHelper.write (_out,value);
	}
}
