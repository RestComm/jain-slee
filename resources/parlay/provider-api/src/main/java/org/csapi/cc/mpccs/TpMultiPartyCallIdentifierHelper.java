package org.csapi.cc.mpccs;


/**
 *	Generated from IDL definition of struct "TpMultiPartyCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiPartyCallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.id(),"TpMultiPartyCallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpMultiPartyCall:1.0", "IpMultiPartyCall"), null),new org.omg.CORBA.StructMember("CallSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.TpMultiPartyCallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mpccs.TpMultiPartyCallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpMultiPartyCallIdentifier:1.0";
	}
	public static org.csapi.cc.mpccs.TpMultiPartyCallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mpccs.TpMultiPartyCallIdentifier result = new org.csapi.cc.mpccs.TpMultiPartyCallIdentifier();
		result.CallReference=org.csapi.cc.mpccs.IpMultiPartyCallHelper.read(in);
		result.CallSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mpccs.TpMultiPartyCallIdentifier s)
	{
		org.csapi.cc.mpccs.IpMultiPartyCallHelper.write(out,s.CallReference);
		out.write_long(s.CallSessionID);
	}
}
