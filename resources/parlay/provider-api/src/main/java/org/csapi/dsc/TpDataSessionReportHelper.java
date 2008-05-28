package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionReport"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionReportHelper.id(),"TpDataSessionReport",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MonitorMode", org.csapi.dsc.TpDataSessionMonitorModeHelper.type(), null),new org.omg.CORBA.StructMember("DataSessionEventTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("DataSessionReportType", org.csapi.dsc.TpDataSessionReportTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalReportInfo", org.csapi.dsc.TpDataSessionAdditionalReportInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionReport s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionReport extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionReport:1.0";
	}
	public static org.csapi.dsc.TpDataSessionReport read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionReport result = new org.csapi.dsc.TpDataSessionReport();
		result.MonitorMode=org.csapi.dsc.TpDataSessionMonitorModeHelper.read(in);
		result.DataSessionEventTime=in.read_string();
		result.DataSessionReportType=org.csapi.dsc.TpDataSessionReportTypeHelper.read(in);
		result.AdditionalReportInfo=org.csapi.dsc.TpDataSessionAdditionalReportInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionReport s)
	{
		org.csapi.dsc.TpDataSessionMonitorModeHelper.write(out,s.MonitorMode);
		out.write_string(s.DataSessionEventTime);
		org.csapi.dsc.TpDataSessionReportTypeHelper.write(out,s.DataSessionReportType);
		org.csapi.dsc.TpDataSessionAdditionalReportInfoHelper.write(out,s.AdditionalReportInfo);
	}
}
