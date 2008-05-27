package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFWExceptionType"
 *	@author JacORB IDL compiler 
 */

public final class TpFWExceptionTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpFWExceptionTypeHelper.id(),"TpFWExceptionType",new String[]{"P_FW_DUMMY"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFWExceptionType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFWExceptionType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFWExceptionType:1.0";
	}
	public static TpFWExceptionType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpFWExceptionType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpFWExceptionType s)
	{
		out.write_long(s.value());
	}
}
