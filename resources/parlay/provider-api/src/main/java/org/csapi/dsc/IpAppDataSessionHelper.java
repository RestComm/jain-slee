package org.csapi.dsc;


/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppDataSessionHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.IpAppDataSession s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.dsc.IpAppDataSession extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/dsc/IpAppDataSession:1.0", "IpAppDataSession");
	}
	public static String id()
	{
		return "IDL:org/csapi/dsc/IpAppDataSession:1.0";
	}
	public static IpAppDataSession read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.dsc.IpAppDataSession s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.dsc.IpAppDataSession narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.dsc.IpAppDataSession)
		{
			return (org.csapi.dsc.IpAppDataSession)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.dsc.IpAppDataSession narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.dsc.IpAppDataSession)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/dsc/IpAppDataSession:1.0"))
			{
				org.csapi.dsc._IpAppDataSessionStub stub;
				stub = new org.csapi.dsc._IpAppDataSessionStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.dsc.IpAppDataSession unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.dsc.IpAppDataSession)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.dsc._IpAppDataSessionStub stub;
				stub = new org.csapi.dsc._IpAppDataSessionStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
