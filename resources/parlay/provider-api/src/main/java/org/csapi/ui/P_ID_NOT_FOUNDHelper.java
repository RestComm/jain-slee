package org.csapi.ui;


/**
 *	Generated from IDL definition of exception "P_ID_NOT_FOUND"
 *	@author JacORB IDL compiler 
 */

public final class P_ID_NOT_FOUNDHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.ui.P_ID_NOT_FOUNDHelper.id(),"P_ID_NOT_FOUND",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.P_ID_NOT_FOUND s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.P_ID_NOT_FOUND extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/P_ID_NOT_FOUND:1.0";
	}
	public static org.csapi.ui.P_ID_NOT_FOUND read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.P_ID_NOT_FOUND result = new org.csapi.ui.P_ID_NOT_FOUND();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.P_ID_NOT_FOUND s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
