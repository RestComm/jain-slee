package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpEntOpAccountInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpEntOpAccountInfoQueryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpEntOpAccountInfoQuery value;
	public IpEntOpAccountInfoQueryHolder()
	{
	}
	public IpEntOpAccountInfoQueryHolder (final IpEntOpAccountInfoQuery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpEntOpAccountInfoQueryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpEntOpAccountInfoQueryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpEntOpAccountInfoQueryHelper.write (_out,value);
	}
}
