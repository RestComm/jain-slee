package org.csapi.cc.mmccs;


/**
 *	Generated from IDL interface "IpMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaCallHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.IpMultiMediaCall s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCall extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpMultiMediaCall:1.0", "IpMultiMediaCall");
	}
	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/IpMultiMediaCall:1.0";
	}
	public static IpMultiMediaCall read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.cc.mmccs.IpMultiMediaCall s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCall narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.cc.mmccs.IpMultiMediaCall)
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCall)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCall narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCall)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/cc/mmccs/IpMultiMediaCall:1.0"))
			{
				org.csapi.cc.mmccs._IpMultiMediaCallStub stub;
				stub = new org.csapi.cc.mmccs._IpMultiMediaCallStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.cc.mmccs.IpMultiMediaCall unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cc.mmccs.IpMultiMediaCall)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.cc.mmccs._IpMultiMediaCallStub stub;
				stub = new org.csapi.cc.mmccs._IpMultiMediaCallStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
