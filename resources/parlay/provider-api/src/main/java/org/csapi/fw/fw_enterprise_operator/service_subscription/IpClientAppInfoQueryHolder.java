package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpClientAppInfoQueryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpClientAppInfoQuery value;
	public IpClientAppInfoQueryHolder()
	{
	}
	public IpClientAppInfoQueryHolder (final IpClientAppInfoQuery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpClientAppInfoQueryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpClientAppInfoQueryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpClientAppInfoQueryHelper.write (_out,value);
	}
}
