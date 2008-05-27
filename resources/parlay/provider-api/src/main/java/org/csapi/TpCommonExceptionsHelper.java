package org.csapi;


/**
 *	Generated from IDL definition of exception "TpCommonExceptions"
 *	@author JacORB IDL compiler 
 */

public final class TpCommonExceptionsHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.csapi.TpCommonExceptionsHelper.id(),"TpCommonExceptions",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ExceptionType", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("ExtraInformation", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpCommonExceptions s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpCommonExceptions extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpCommonExceptions:1.0";
	}
	public static org.csapi.TpCommonExceptions read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpCommonExceptions result = new org.csapi.TpCommonExceptions();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.ExceptionType=in.read_long();
		result.ExtraInformation=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpCommonExceptions s)
	{
		out.write_string(id());
		out.write_long(s.ExceptionType);
		out.write_string(s.ExtraInformation);
	}
}
