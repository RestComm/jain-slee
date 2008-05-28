package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpMultiMediaCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.id(),"TpMultiMediaCallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MMCallReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpMultiMediaCall:1.0", "IpMultiMediaCall"), null),new org.omg.CORBA.StructMember("MMCallSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMultiMediaCallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMultiMediaCallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMultiMediaCallIdentifier:1.0";
	}
	public static org.csapi.cc.mmccs.TpMultiMediaCallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpMultiMediaCallIdentifier result = new org.csapi.cc.mmccs.TpMultiMediaCallIdentifier();
		result.MMCallReference=org.csapi.cc.mmccs.IpMultiMediaCallHelper.read(in);
		result.MMCallSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpMultiMediaCallIdentifier s)
	{
		org.csapi.cc.mmccs.IpMultiMediaCallHelper.write(out,s.MMCallReference);
		out.write_long(s.MMCallSessionID);
	}
}
