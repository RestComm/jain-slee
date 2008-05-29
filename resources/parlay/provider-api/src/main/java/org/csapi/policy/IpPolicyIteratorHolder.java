package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyIterator"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPolicyIterator value;
	public IpPolicyIteratorHolder()
	{
	}
	public IpPolicyIteratorHolder (final IpPolicyIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPolicyIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPolicyIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPolicyIteratorHelper.write (_out,value);
	}
}
