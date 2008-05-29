package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpClientAppManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpClientAppManagement value;
	public IpClientAppManagementHolder()
	{
	}
	public IpClientAppManagementHolder (final IpClientAppManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpClientAppManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpClientAppManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpClientAppManagementHelper.write (_out,value);
	}
}
