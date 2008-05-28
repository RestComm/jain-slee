package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceContractManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceContractManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceContractManagement value;
	public IpServiceContractManagementHolder()
	{
	}
	public IpServiceContractManagementHolder (final IpServiceContractManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceContractManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceContractManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceContractManagementHelper.write (_out,value);
	}
}
