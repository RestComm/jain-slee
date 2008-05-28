package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpMultiMediaCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallLegIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierHelper.id(),"TpMultiMediaCallLegIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MMCallLegReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpMultiMediaCallLeg:1.0", "IpMultiMediaCallLeg"), null),new org.omg.CORBA.StructMember("MMCallLegSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMultiMediaCallLegIdentifier:1.0";
	}
	public static org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier result = new org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier();
		result.MMCallLegReference=org.csapi.cc.mmccs.IpMultiMediaCallLegHelper.read(in);
		result.MMCallLegSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier s)
	{
		org.csapi.cc.mmccs.IpMultiMediaCallLegHelper.write(out,s.MMCallLegReference);
		out.write_long(s.MMCallLegSessionID);
	}
}
