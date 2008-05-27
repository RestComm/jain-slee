package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallErrorTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallErrorTypeHelper.id(),"TpCallErrorType",new String[]{"P_CALL_ERROR_UNDEFINED","P_CALL_ERROR_INVALID_ADDRESS","P_CALL_ERROR_INVALID_STATE","P_CALL_ERROR_RESOURCE_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallErrorType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallErrorType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallErrorType:1.0";
	}
	public static TpCallErrorType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallErrorType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallErrorType s)
	{
		out.write_long(s.value());
	}
}
