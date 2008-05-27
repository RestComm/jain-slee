package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceProfileManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceProfileManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceProfileManagement value;
	public IpServiceProfileManagementHolder()
	{
	}
	public IpServiceProfileManagementHolder (final IpServiceProfileManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceProfileManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceProfileManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceProfileManagementHelper.write (_out,value);
	}
}
