package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMCapabilityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMCapabilityManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMCapabilityManagement value;
	public IpPAMCapabilityManagementHolder()
	{
	}
	public IpPAMCapabilityManagementHolder (final IpPAMCapabilityManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMCapabilityManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMCapabilityManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMCapabilityManagementHelper.write (_out,value);
	}
}
