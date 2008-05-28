package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.gccs.TpCallReportTypeHelper.id(),"TpCallReportType",new String[]{"P_CALL_REPORT_UNDEFINED","P_CALL_REPORT_PROGRESS","P_CALL_REPORT_ALERTING","P_CALL_REPORT_ANSWER","P_CALL_REPORT_BUSY","P_CALL_REPORT_NO_ANSWER","P_CALL_REPORT_DISCONNECT","P_CALL_REPORT_REDIRECTED","P_CALL_REPORT_SERVICE_CODE","P_CALL_REPORT_ROUTING_FAILURE","P_CALL_REPORT_QUEUED","P_CALL_REPORT_NOT_REACHABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallReportType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallReportType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallReportType:1.0";
	}
	public static TpCallReportType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallReportType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallReportType s)
	{
		out.write_long(s.value());
	}
}
