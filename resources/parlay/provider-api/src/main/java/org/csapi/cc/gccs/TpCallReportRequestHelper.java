package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallReportRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallReportRequestHelper.id(),"TpCallReportRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MonitorMode", org.csapi.cc.TpCallMonitorModeHelper.type(), null),new org.omg.CORBA.StructMember("CallReportType", org.csapi.cc.gccs.TpCallReportTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalReportCriteria", org.csapi.cc.gccs.TpCallAdditionalReportCriteriaHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallReportRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallReportRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallReportRequest:1.0";
	}
	public static org.csapi.cc.gccs.TpCallReportRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallReportRequest result = new org.csapi.cc.gccs.TpCallReportRequest();
		result.MonitorMode=org.csapi.cc.TpCallMonitorModeHelper.read(in);
		result.CallReportType=org.csapi.cc.gccs.TpCallReportTypeHelper.read(in);
		result.AdditionalReportCriteria=org.csapi.cc.gccs.TpCallAdditionalReportCriteriaHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallReportRequest s)
	{
		org.csapi.cc.TpCallMonitorModeHelper.write(out,s.MonitorMode);
		org.csapi.cc.gccs.TpCallReportTypeHelper.write(out,s.CallReportType);
		org.csapi.cc.gccs.TpCallAdditionalReportCriteriaHelper.write(out,s.AdditionalReportCriteria);
	}
}
