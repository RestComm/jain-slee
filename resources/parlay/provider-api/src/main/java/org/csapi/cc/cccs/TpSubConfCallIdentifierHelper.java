package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpSubConfCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpSubConfCallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpSubConfCallIdentifierHelper.id(),"TpSubConfCallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("SubConfCallReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/cccs/IpSubConfCall:1.0", "IpSubConfCall"), null),new org.omg.CORBA.StructMember("SubConfCallSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpSubConfCallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpSubConfCallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpSubConfCallIdentifier:1.0";
	}
	public static org.csapi.cc.cccs.TpSubConfCallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpSubConfCallIdentifier result = new org.csapi.cc.cccs.TpSubConfCallIdentifier();
		result.SubConfCallReference=org.csapi.cc.cccs.IpSubConfCallHelper.read(in);
		result.SubConfCallSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpSubConfCallIdentifier s)
	{
		org.csapi.cc.cccs.IpSubConfCallHelper.write(out,s.SubConfCallReference);
		out.write_long(s.SubConfCallSessionID);
	}
}
