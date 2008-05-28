package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIError"
 *	@author JacORB IDL compiler 
 */

public final class TpUIErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIErrorHelper.id(),"TpUIError",new String[]{"P_UI_ERROR_UNDEFINED","P_UI_ERROR_ILLEGAL_INFO","P_UI_ERROR_ID_NOT_FOUND","P_UI_ERROR_RESOURCE_UNAVAILABLE","P_UI_ERROR_ILLEGAL_RANGE","P_UI_ERROR_IMPROPER_USER_RESPONSE","P_UI_ERROR_ABANDON","P_UI_ERROR_NO_OPERATION_ACTIVE","P_UI_ERROR_NO_SPACE_AVAILABLE","P_UI_ERROR_RESOURCE_TIMEOUT"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIError:1.0";
	}
	public static TpUIError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIError s)
	{
		out.write_long(s.value());
	}
}
