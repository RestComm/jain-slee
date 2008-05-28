package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpServiceProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfileHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpServiceProfileHelper.id(),"TpServiceProfile",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ServiceProfileID", org.csapi.fw.TpServiceProfileIDHelper.type(), null),new org.omg.CORBA.StructMember("ServiceProfileDescription", org.csapi.fw.TpServiceProfileDescriptionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpServiceProfile s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpServiceProfile extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpServiceProfile:1.0";
	}
	public static org.csapi.fw.TpServiceProfile read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpServiceProfile result = new org.csapi.fw.TpServiceProfile();
		result.ServiceProfileID=in.read_string();
		result.ServiceProfileDescription=org.csapi.fw.TpServiceProfileDescriptionHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpServiceProfile s)
	{
		out.write_string(s.ServiceProfileID);
		org.csapi.fw.TpServiceProfileDescriptionHelper.write(out,s.ServiceProfileDescription);
	}
}
