package org.csapi;


/**
 *	Generated from IDL definition of struct "TpAoCInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpAoCInfoHelper.id(),"TpAoCInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargeOrder", org.csapi.TpAoCOrderHelper.type(), null),new org.omg.CORBA.StructMember("Currency", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpAoCInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpAoCInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpAoCInfo:1.0";
	}
	public static org.csapi.TpAoCInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpAoCInfo result = new org.csapi.TpAoCInfo();
		result.ChargeOrder=org.csapi.TpAoCOrderHelper.read(in);
		result.Currency=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpAoCInfo s)
	{
		org.csapi.TpAoCOrderHelper.write(out,s.ChargeOrder);
		out.write_string(s.Currency);
	}
}
