package org.csapi.fw.fw_application.service_agreement;


/**
 *	Generated from IDL interface "IpServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceAgreementManagementHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/fw/fw_application/service_agreement/IpServiceAgreementManagement:1.0", "IpServiceAgreementManagement");
	}
	public static String id()
	{
		return "IDL:org/csapi/fw/fw_application/service_agreement/IpServiceAgreementManagement:1.0";
	}
	public static IpServiceAgreementManagement read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement)
		{
			return (org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/fw/fw_application/service_agreement/IpServiceAgreementManagement:1.0"))
			{
				org.csapi.fw.fw_application.service_agreement._IpServiceAgreementManagementStub stub;
				stub = new org.csapi.fw.fw_application.service_agreement._IpServiceAgreementManagementStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.fw.fw_application.service_agreement._IpServiceAgreementManagementStub stub;
				stub = new org.csapi.fw.fw_application.service_agreement._IpServiceAgreementManagementStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
