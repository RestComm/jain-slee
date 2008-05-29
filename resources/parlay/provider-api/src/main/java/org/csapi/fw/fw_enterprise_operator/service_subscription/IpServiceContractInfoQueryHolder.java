package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceContractInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceContractInfoQueryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceContractInfoQuery value;
	public IpServiceContractInfoQueryHolder()
	{
	}
	public IpServiceContractInfoQueryHolder (final IpServiceContractInfoQuery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceContractInfoQueryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceContractInfoQueryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceContractInfoQueryHelper.write (_out,value);
	}
}
