package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpSagProfilePair"
 *	@author JacORB IDL compiler 
 */

public final class TpSagProfilePairHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpSagProfilePairHelper.id(),"TpSagProfilePair",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Sag", org.csapi.fw.TpSagIDHelper.type(), null),new org.omg.CORBA.StructMember("ServiceProfile", org.csapi.fw.TpServiceProfileIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpSagProfilePair s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpSagProfilePair extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpSagProfilePair:1.0";
	}
	public static org.csapi.fw.TpSagProfilePair read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpSagProfilePair result = new org.csapi.fw.TpSagProfilePair();
		result.Sag=in.read_string();
		result.ServiceProfile=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpSagProfilePair s)
	{
		out.write_string(s.Sag);
		out.write_string(s.ServiceProfile);
	}
}
