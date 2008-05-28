package org.csapi.fw;


/**
 *	Generated from IDL definition of exception "P_INVALID_ADDITION_TO_SAG"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_ADDITION_TO_SAGHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.id(),"P_INVALID_ADDITION_TO_SAG",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.P_INVALID_ADDITION_TO_SAG s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.P_INVALID_ADDITION_TO_SAG extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/P_INVALID_ADDITION_TO_SAG:1.0";
	}
	public static org.csapi.fw.P_INVALID_ADDITION_TO_SAG read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.P_INVALID_ADDITION_TO_SAG result = new org.csapi.fw.P_INVALID_ADDITION_TO_SAG();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.P_INVALID_ADDITION_TO_SAG s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
