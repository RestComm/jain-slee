package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionErrorTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.dsc.TpDataSessionErrorTypeHelper.id(),"TpDataSessionErrorType",new String[]{"P_DATA_SESSION_ERROR_UNDEFINED","P_DATA_SESSION_ERROR_INVALID_ADDRESS","P_DATA_SESSION_ERROR_INVALID_STATE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionErrorType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionErrorType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionErrorType:1.0";
	}
	public static TpDataSessionErrorType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionErrorType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionErrorType s)
	{
		out.write_long(s.value());
	}
}
