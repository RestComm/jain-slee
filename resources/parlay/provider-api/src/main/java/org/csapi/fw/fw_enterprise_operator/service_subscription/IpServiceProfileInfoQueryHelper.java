package org.csapi.fw.fw_enterprise_operator.service_subscription;


/**
 *	Generated from IDL interface "IpServiceProfileInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceProfileInfoQueryHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceProfileInfoQuery:1.0", "IpServiceProfileInfoQuery");
	}
	public static String id()
	{
		return "IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceProfileInfoQuery:1.0";
	}
	public static IpServiceProfileInfoQuery read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery)
		{
			return (org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceProfileInfoQuery:1.0"))
			{
				org.csapi.fw.fw_enterprise_operator.service_subscription._IpServiceProfileInfoQueryStub stub;
				stub = new org.csapi.fw.fw_enterprise_operator.service_subscription._IpServiceProfileInfoQueryStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.fw.fw_enterprise_operator.service_subscription._IpServiceProfileInfoQueryStub stub;
				stub = new org.csapi.fw.fw_enterprise_operator.service_subscription._IpServiceProfileInfoQueryStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
