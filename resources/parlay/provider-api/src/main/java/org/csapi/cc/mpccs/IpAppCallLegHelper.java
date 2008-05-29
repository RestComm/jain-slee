package org.csapi.cc.mpccs;


/**
 *	Generated from IDL interface "IpAppCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppCallLegHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.IpAppCallLeg s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.cc.mpccs.IpAppCallLeg extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0", "IpAppCallLeg");
	}
	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0";
	}
	public static IpAppCallLeg read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.cc.mpccs.IpAppCallLeg s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.cc.mpccs.IpAppCallLeg narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.cc.mpccs.IpAppCallLeg)
		{
			return (org.csapi.cc.mpccs.IpAppCallLeg)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.cc.mpccs.IpAppCallLeg narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mpccs.IpAppCallLeg)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0"))
			{
				org.csapi.cc.mpccs._IpAppCallLegStub stub;
				stub = new org.csapi.cc.mpccs._IpAppCallLegStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.cc.mpccs.IpAppCallLeg unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mpccs.IpAppCallLeg)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.cc.mpccs._IpAppCallLegStub stub;
				stub = new org.csapi.cc.mpccs._IpAppCallLegStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
