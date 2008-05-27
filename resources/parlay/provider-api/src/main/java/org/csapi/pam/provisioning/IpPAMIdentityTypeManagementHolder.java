package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMIdentityTypeManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMIdentityTypeManagement value;
	public IpPAMIdentityTypeManagementHolder()
	{
	}
	public IpPAMIdentityTypeManagementHolder (final IpPAMIdentityTypeManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMIdentityTypeManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMIdentityTypeManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMIdentityTypeManagementHelper.write (_out,value);
	}
}
