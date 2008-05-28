package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpEntOpAccountManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpEntOpAccountManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpEntOpAccountManagement value;
	public IpEntOpAccountManagementHolder()
	{
	}
	public IpEntOpAccountManagementHolder (final IpEntOpAccountManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpEntOpAccountManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpEntOpAccountManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpEntOpAccountManagementHelper.write (_out,value);
	}
}
