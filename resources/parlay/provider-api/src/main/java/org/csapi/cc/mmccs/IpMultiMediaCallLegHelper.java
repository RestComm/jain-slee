package org.csapi.cc.mmccs;


/**
 *	Generated from IDL interface "IpMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaCallLegHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.IpMultiMediaCallLeg s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCallLeg extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpMultiMediaCallLeg:1.0", "IpMultiMediaCallLeg");
	}
	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/IpMultiMediaCallLeg:1.0";
	}
	public static IpMultiMediaCallLeg read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.cc.mmccs.IpMultiMediaCallLeg s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCallLeg narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.cc.mmccs.IpMultiMediaCallLeg)
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCallLeg)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCallLeg narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCallLeg)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/cc/mmccs/IpMultiMediaCallLeg:1.0"))
			{
				org.csapi.cc.mmccs._IpMultiMediaCallLegStub stub;
				stub = new org.csapi.cc.mmccs._IpMultiMediaCallLegStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCallLeg unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCallLeg)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.cc.mmccs._IpMultiMediaCallLegStub stub;
				stub = new org.csapi.cc.mmccs._IpMultiMediaCallLegStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
