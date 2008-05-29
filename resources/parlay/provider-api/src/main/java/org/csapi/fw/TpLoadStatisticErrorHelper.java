package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpLoadStatisticErrorHelper.id(),"TpLoadStatisticError",new String[]{"P_LOAD_INFO_ERROR_UNDEFINED","P_LOAD_INFO_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatisticError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatisticError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatisticError:1.0";
	}
	public static TpLoadStatisticError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLoadStatisticError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLoadStatisticError s)
	{
		out.write_long(s.value());
	}
}
