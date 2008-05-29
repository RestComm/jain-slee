package org.csapi.pam.access;


/**
 *	Generated from IDL interface "IpPAMAvailability"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAvailabilityHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.access.IpPAMAvailability s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.pam.access.IpPAMAvailability extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/pam/access/IpPAMAvailability:1.0", "IpPAMAvailability");
	}
	public static String id()
	{
		return "IDL:org/csapi/pam/access/IpPAMAvailability:1.0";
	}
	public static IpPAMAvailability read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.pam.access.IpPAMAvailability s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.pam.access.IpPAMAvailability narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.pam.access.IpPAMAvailability)
		{
			return (org.csapi.pam.access.IpPAMAvailability)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.pam.access.IpPAMAvailability narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.pam.access.IpPAMAvailability)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/pam/access/IpPAMAvailability:1.0"))
			{
				org.csapi.pam.access._IpPAMAvailabilityStub stub;
				stub = new org.csapi.pam.access._IpPAMAvailabilityStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.pam.access.IpPAMAvailability unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.pam.access.IpPAMAvailability)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.pam.access._IpPAMAvailabilityStub stub;
				stub = new org.csapi.pam.access._IpPAMAvailabilityStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
