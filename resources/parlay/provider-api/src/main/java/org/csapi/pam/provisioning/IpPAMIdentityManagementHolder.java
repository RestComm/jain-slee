package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMIdentityManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMIdentityManagement value;
	public IpPAMIdentityManagementHolder()
	{
	}
	public IpPAMIdentityManagementHolder (final IpPAMIdentityManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMIdentityManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMIdentityManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMIdentityManagementHelper.write (_out,value);
	}
}
