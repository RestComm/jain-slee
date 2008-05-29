package org.csapi.cc.mpccs;


/**
 *	Generated from IDL definition of struct "TpCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mpccs.TpCallLegIdentifierHelper.id(),"TpCallLegIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallLegReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpCallLeg:1.0", "IpCallLeg"), null),new org.omg.CORBA.StructMember("CallLegSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.TpCallLegIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mpccs.TpCallLegIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpCallLegIdentifier:1.0";
	}
	public static org.csapi.cc.mpccs.TpCallLegIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mpccs.TpCallLegIdentifier result = new org.csapi.cc.mpccs.TpCallLegIdentifier();
		result.CallLegReference=org.csapi.cc.mpccs.IpCallLegHelper.read(in);
		result.CallLegSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mpccs.TpCallLegIdentifier s)
	{
		org.csapi.cc.mpccs.IpCallLegHelper.write(out,s.CallLegReference);
		out.write_long(s.CallLegSessionID);
	}
}
