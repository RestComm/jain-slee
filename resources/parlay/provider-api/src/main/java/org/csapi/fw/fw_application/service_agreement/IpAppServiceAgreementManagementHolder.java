package org.csapi.fw.fw_application.service_agreement;

/**
 *	Generated from IDL interface "IpAppServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppServiceAgreementManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppServiceAgreementManagement value;
	public IpAppServiceAgreementManagementHolder()
	{
	}
	public IpAppServiceAgreementManagementHolder (final IpAppServiceAgreementManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppServiceAgreementManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppServiceAgreementManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppServiceAgreementManagementHelper.write (_out,value);
	}
}
