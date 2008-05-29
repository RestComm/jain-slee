package org.csapi.fw.fw_application.service_agreement;

/**
 *	Generated from IDL interface "IpServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceAgreementManagementHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceAgreementManagement value;
	public IpServiceAgreementManagementHolder()
	{
	}
	public IpServiceAgreementManagementHolder (final IpServiceAgreementManagement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceAgreementManagementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceAgreementManagementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceAgreementManagementHelper.write (_out,value);
	}
}
