package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallLegInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegInfoReportHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallLegInfoReportHelper.id(),"TpCallLegInfoReport",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallLegInfoType", org.csapi.cc.TpCallLegInfoTypeHelper.type(), null),new org.omg.CORBA.StructMember("CallLegStartTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallLegConnectedToResourceTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallLegConnectedToAddressTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallLegEndTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("ConnectedAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("CallLegReleaseCause", org.csapi.cc.TpReleaseCauseHelper.type(), null),new org.omg.CORBA.StructMember("CallAppInfo", org.csapi.cc.TpCallAppInfoSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallLegInfoReport s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallLegInfoReport extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallLegInfoReport:1.0";
	}
	public static org.csapi.cc.TpCallLegInfoReport read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallLegInfoReport result = new org.csapi.cc.TpCallLegInfoReport();
		result.CallLegInfoType=in.read_long();
		result.CallLegStartTime=in.read_string();
		result.CallLegConnectedToResourceTime=in.read_string();
		result.CallLegConnectedToAddressTime=in.read_string();
		result.CallLegEndTime=in.read_string();
		result.ConnectedAddress=org.csapi.TpAddressHelper.read(in);
		result.CallLegReleaseCause=org.csapi.cc.TpReleaseCauseHelper.read(in);
		result.CallAppInfo = org.csapi.cc.TpCallAppInfoSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallLegInfoReport s)
	{
		out.write_long(s.CallLegInfoType);
		out.write_string(s.CallLegStartTime);
		out.write_string(s.CallLegConnectedToResourceTime);
		out.write_string(s.CallLegConnectedToAddressTime);
		out.write_string(s.CallLegEndTime);
		org.csapi.TpAddressHelper.write(out,s.ConnectedAddress);
		org.csapi.cc.TpReleaseCauseHelper.write(out,s.CallLegReleaseCause);
		org.csapi.cc.TpCallAppInfoSetHelper.write(out,s.CallAppInfo);
	}
}
