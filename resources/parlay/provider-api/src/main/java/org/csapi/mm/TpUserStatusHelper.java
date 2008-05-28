package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUserStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUserStatusHelper.id(),"TpUserStatus",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UserID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("StatusCode", org.csapi.mm.TpMobilityErrorHelper.type(), null),new org.omg.CORBA.StructMember("Status", org.csapi.mm.TpUserStatusIndicatorHelper.type(), null),new org.omg.CORBA.StructMember("TerminalType", org.csapi.mm.TpTerminalTypeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserStatus s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserStatus extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserStatus:1.0";
	}
	public static org.csapi.mm.TpUserStatus read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUserStatus result = new org.csapi.mm.TpUserStatus();
		result.UserID=org.csapi.TpAddressHelper.read(in);
		result.StatusCode=org.csapi.mm.TpMobilityErrorHelper.read(in);
		result.Status=org.csapi.mm.TpUserStatusIndicatorHelper.read(in);
		result.TerminalType=org.csapi.mm.TpTerminalTypeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUserStatus s)
	{
		org.csapi.TpAddressHelper.write(out,s.UserID);
		org.csapi.mm.TpMobilityErrorHelper.write(out,s.StatusCode);
		org.csapi.mm.TpUserStatusIndicatorHelper.write(out,s.Status);
		org.csapi.mm.TpTerminalTypeHelper.write(out,s.TerminalType);
	}
}
