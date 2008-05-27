package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpInitial"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpInitialHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpInitial value;
	public IpInitialHolder()
	{
	}
	public IpInitialHolder (final IpInitial initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpInitialHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpInitialHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpInitialHelper.write (_out,value);
	}
}
