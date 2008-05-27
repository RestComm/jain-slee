package org.csapi;


/**
 *	Generated from IDL interface "IpService"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.IpService s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.IpService extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/IpService:1.0", "IpService");
	}
	public static String id()
	{
		return "IDL:org/csapi/IpService:1.0";
	}
	public static IpService read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.IpService s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.IpService narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.IpService)
		{
			return (org.csapi.IpService)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.IpService narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.IpService)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/IpService:1.0"))
			{
				org.csapi._IpServiceStub stub;
				stub = new org.csapi._IpServiceStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.IpService unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.IpService)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi._IpServiceStub stub;
				stub = new org.csapi._IpServiceStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
