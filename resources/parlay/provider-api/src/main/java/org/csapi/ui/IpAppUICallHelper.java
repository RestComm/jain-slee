package org.csapi.ui;


/**
 *	Generated from IDL interface "IpAppUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppUICallHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.IpAppUICall s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.ui.IpAppUICall extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/ui/IpAppUICall:1.0", "IpAppUICall");
	}
	public static String id()
	{
		return "IDL:org/csapi/ui/IpAppUICall:1.0";
	}
	public static IpAppUICall read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.ui.IpAppUICall s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.ui.IpAppUICall narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.ui.IpAppUICall)
		{
			return (org.csapi.ui.IpAppUICall)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.ui.IpAppUICall narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.ui.IpAppUICall)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/ui/IpAppUICall:1.0"))
			{
				org.csapi.ui._IpAppUICallStub stub;
				stub = new org.csapi.ui._IpAppUICallStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.ui.IpAppUICall unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.ui.IpAppUICall)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.ui._IpAppUICallStub stub;
				stub = new org.csapi.ui._IpAppUICallStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
