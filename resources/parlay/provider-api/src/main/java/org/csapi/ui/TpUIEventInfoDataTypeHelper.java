package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIEventInfoDataType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventInfoDataTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIEventInfoDataTypeHelper.id(),"TpUIEventInfoDataType",new String[]{"P_UI_EVENT_DATA_TYPE_UNDEFINED","P_UI_EVENT_DATA_TYPE_UNSPECIFIED","P_UI_EVENT_DATA_TYPE_TEXT","P_UI_EVENT_DATA_TYPE_USSD_DATA"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIEventInfoDataType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIEventInfoDataType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIEventInfoDataType:1.0";
	}
	public static TpUIEventInfoDataType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIEventInfoDataType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIEventInfoDataType s)
	{
		out.write_long(s.value());
	}
}
