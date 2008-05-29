package org.csapi.cm;


/**
 *	Generated from IDL definition of exception "P_UNKNOWN_DESCRIPTION"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_DESCRIPTIONHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.cm.P_UNKNOWN_DESCRIPTIONHelper.id(),"P_UNKNOWN_DESCRIPTION",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.P_UNKNOWN_DESCRIPTION s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.P_UNKNOWN_DESCRIPTION extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/P_UNKNOWN_DESCRIPTION:1.0";
	}
	public static org.csapi.cm.P_UNKNOWN_DESCRIPTION read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.P_UNKNOWN_DESCRIPTION result = new org.csapi.cm.P_UNKNOWN_DESCRIPTION();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.P_UNKNOWN_DESCRIPTION s)
	{
		out.write_string(id());
		out.write_string(s.ExtraInformation);
	}
}
