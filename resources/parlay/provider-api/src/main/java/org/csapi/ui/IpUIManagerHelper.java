package org.csapi.ui;


/**
 *	Generated from IDL interface "IpUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpUIManagerHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.IpUIManager s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.ui.IpUIManager extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/ui/IpUIManager:1.0", "IpUIManager");
	}
	public static String id()
	{
		return "IDL:org/csapi/ui/IpUIManager:1.0";
	}
	public static IpUIManager read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.ui.IpUIManager s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.ui.IpUIManager narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.ui.IpUIManager)
		{
			return (org.csapi.ui.IpUIManager)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.ui.IpUIManager narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.ui.IpUIManager)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/ui/IpUIManager:1.0"))
			{
				org.csapi.ui._IpUIManagerStub stub;
				stub = new org.csapi.ui._IpUIManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.ui.IpUIManager unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.ui.IpUIManager)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.ui._IpUIManagerStub stub;
				stub = new org.csapi.ui._IpUIManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
