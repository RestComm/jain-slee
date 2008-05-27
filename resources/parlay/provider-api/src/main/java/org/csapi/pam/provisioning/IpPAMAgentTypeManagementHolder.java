package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAgentTypeManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMAgentTypeManagement value;
	public IpPAMAgentTypeManagementHolder()
	{
	}
	public IpPAMAgentTypeManagementHolder (final IpPAMAgentTypeManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMAgentTypeManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMAgentTypeManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMAgentTypeManagementHelper.write (_out,value);
	}
}
