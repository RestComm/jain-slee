package org.csapi.fw.fw_service.service_registration;


/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwServiceRegistrationHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/fw/fw_service/service_registration/IpFwServiceRegistration:1.0", "IpFwServiceRegistration");
	}
	public static String id()
	{
		return "IDL:org/csapi/fw/fw_service/service_registration/IpFwServiceRegistration:1.0";
	}
	public static IpFwServiceRegistration read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration)
		{
			return (org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/fw/fw_service/service_registration/IpFwServiceRegistration:1.0"))
			{
				org.csapi.fw.fw_service.service_registration._IpFwServiceRegistrationStub stub;
				stub = new org.csapi.fw.fw_service.service_registration._IpFwServiceRegistrationStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.fw.fw_service.service_registration._IpFwServiceRegistrationStub stub;
				stub = new org.csapi.fw.fw_service.service_registration._IpFwServiceRegistrationStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
