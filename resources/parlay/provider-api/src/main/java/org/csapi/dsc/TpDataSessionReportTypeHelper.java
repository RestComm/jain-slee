package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.dsc.TpDataSessionReportTypeHelper.id(),"TpDataSessionReportType",new String[]{"P_DATA_SESSION_REPORT_UNDEFINED","P_DATA_SESSION_REPORT_CONNECTED","P_DATA_SESSION_REPORT_DISCONNECT"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionReportType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionReportType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionReportType:1.0";
	}
	public static TpDataSessionReportType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionReportType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionReportType s)
	{
		out.write_long(s.value());
	}
}
