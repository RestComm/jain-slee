package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfoTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIInfoTypeHelper.id(),"TpUIInfoType",new String[]{"P_UI_INFO_ID","P_UI_INFO_DATA","P_UI_INFO_ADDRESS","P_UI_INFO_BIN_DATA","P_UI_INFO_UUENCODED","P_UI_INFO_MIME","P_UI_INFO_WAVE","P_UI_INFO_AU"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIInfoType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIInfoType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIInfoType:1.0";
	}
	public static TpUIInfoType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIInfoType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIInfoType s)
	{
		out.write_long(s.value());
	}
}
