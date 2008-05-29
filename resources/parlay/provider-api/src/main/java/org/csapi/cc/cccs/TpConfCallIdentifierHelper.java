package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpConfCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpConfCallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpConfCallIdentifierHelper.id(),"TpConfCallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ConfCallReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/cccs/IpConfCall:1.0", "IpConfCall"), null),new org.omg.CORBA.StructMember("ConfCallSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpConfCallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpConfCallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpConfCallIdentifier:1.0";
	}
	public static org.csapi.cc.cccs.TpConfCallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpConfCallIdentifier result = new org.csapi.cc.cccs.TpConfCallIdentifier();
		result.ConfCallReference=org.csapi.cc.cccs.IpConfCallHelper.read(in);
		result.ConfCallSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpConfCallIdentifier s)
	{
		org.csapi.cc.cccs.IpConfCallHelper.write(out,s.ConfCallReference);
		out.write_long(s.ConfCallSessionID);
	}
}
