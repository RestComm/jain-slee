package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIReport"
 *	@author JacORB IDL compiler 
 */

public final class TpUIReportHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIReportHelper.id(),"TpUIReport",new String[]{"P_UI_REPORT_UNDEFINED","P_UI_REPORT_INFO_SENT","P_UI_REPORT_INFO_COLLECTED","P_UI_REPORT_NO_INPUT","P_UI_REPORT_TIMEOUT","P_UI_REPORT_MESSAGE_STORED","P_UI_REPORT_MESSAGE_NOT_STORED","P_UI_REPORT_MESSAGE_DELETED","P_UI_REPORT_MESSAGE_NOT_DELETED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIReport s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIReport extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIReport:1.0";
	}
	public static TpUIReport read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIReport.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIReport s)
	{
		out.write_long(s.value());
	}
}
