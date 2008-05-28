package org.csapi.fw;


/**
 *	Generated from IDL definition of exception "P_PROPERTY_TYPE_MISMATCH"
 *	@author JacORB IDL compiler 
 */

public final class P_PROPERTY_TYPE_MISMATCHHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.id(),"P_PROPERTY_TYPE_MISMATCH",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.P_PROPERTY_TYPE_MISMATCH s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.P_PROPERTY_TYPE_MISMATCH extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/P_PROPERTY_TYPE_MISMATCH:1.0";
	}
	public static org.csapi.fw.P_PROPERTY_TYPE_MISMATCH read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.P_PROPERTY_TYPE_MISMATCH result = new org.csapi.fw.P_PROPERTY_TYPE_MISMATCH();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.P_PROPERTY_TYPE_MISMATCH s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
