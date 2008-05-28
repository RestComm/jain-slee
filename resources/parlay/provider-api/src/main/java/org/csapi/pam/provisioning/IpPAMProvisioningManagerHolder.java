package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMProvisioningManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMProvisioningManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMProvisioningManager value;
	public IpPAMProvisioningManagerHolder()
	{
	}
	public IpPAMProvisioningManagerHolder (final IpPAMProvisioningManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMProvisioningManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMProvisioningManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMProvisioningManagerHelper.write (_out,value);
	}
}
