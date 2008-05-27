package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallIdentifierHelper.id(),"TpCallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/gccs/IpCall:1.0", "IpCall"), null),new org.omg.CORBA.StructMember("CallSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallIdentifier:1.0";
	}
	public static org.csapi.cc.gccs.TpCallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallIdentifier result = new org.csapi.cc.gccs.TpCallIdentifier();
		result.CallReference=org.csapi.cc.gccs.IpCallHelper.read(in);
		result.CallSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallIdentifier s)
	{
		org.csapi.cc.gccs.IpCallHelper.write(out,s.CallReference);
		out.write_long(s.CallSessionID);
	}
}
