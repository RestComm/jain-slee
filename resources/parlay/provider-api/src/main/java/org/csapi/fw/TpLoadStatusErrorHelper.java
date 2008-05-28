package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatusError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatusErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpLoadStatusErrorHelper.id(),"TpLoadStatusError",new String[]{"LOAD_STATUS_ERROR_UNDEFINED","LOAD_STATUS_ERROR_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatusError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatusError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatusError:1.0";
	}
	public static TpLoadStatusError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLoadStatusError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLoadStatusError s)
	{
		out.write_long(s.value());
	}
}
