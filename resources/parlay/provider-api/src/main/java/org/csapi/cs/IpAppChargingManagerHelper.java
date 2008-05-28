package org.csapi.cs;


/**
 *	Generated from IDL interface "IpAppChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppChargingManagerHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.IpAppChargingManager s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.cs.IpAppChargingManager extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cs/IpAppChargingManager:1.0", "IpAppChargingManager");
	}
	public static String id()
	{
		return "IDL:org/csapi/cs/IpAppChargingManager:1.0";
	}
	public static IpAppChargingManager read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.cs.IpAppChargingManager s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.cs.IpAppChargingManager narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.cs.IpAppChargingManager)
		{
			return (org.csapi.cs.IpAppChargingManager)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.cs.IpAppChargingManager narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cs.IpAppChargingManager)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/cs/IpAppChargingManager:1.0"))
			{
				org.csapi.cs._IpAppChargingManagerStub stub;
				stub = new org.csapi.cs._IpAppChargingManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.cs.IpAppChargingManager unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.cs.IpAppChargingManager)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.cs._IpAppChargingManagerStub stub;
				stub = new org.csapi.cs._IpAppChargingManagerStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
