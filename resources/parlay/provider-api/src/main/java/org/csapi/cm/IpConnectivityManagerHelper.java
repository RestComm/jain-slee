package org.csapi.cm;


/**
 *	Generated from IDL interface "IpConnectivityManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpConnectivityManagerHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.IpConnectivityManager s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.cm.IpConnectivityManager extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cm/IpConnectivityManager:1.0", "IpConnectivityManager");
	}
	public static String id()
	{
		return "IDL:org/csapi/cm/IpConnectivityManager:1.0";
	}
	public static IpConnectivityManager read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.cm.IpConnectivityManager s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.cm.IpConnectivityManager narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.cm.IpConnectivityManager)
		{
			return (org.csapi.cm.IpConnectivityManager)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.cm.IpConnectivityManager narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cm.IpConnectivityManager)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/cm/IpConnectivityManager:1.0"))
			{
				org.csapi.cm._IpConnectivityManagerStub stub;
				stub = new org.csapi.cm._IpConnectivityManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.cm.IpConnectivityManager unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cm.IpConnectivityManager)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.cm._IpConnectivityManagerStub stub;
				stub = new org.csapi.cm._IpConnectivityManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
