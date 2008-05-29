package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAgentManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMAgentManagement value;
	public IpPAMAgentManagementHolder()
	{
	}
	public IpPAMAgentManagementHolder (final IpPAMAgentManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMAgentManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMAgentManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMAgentManagementHelper.write (_out,value);
	}
}
