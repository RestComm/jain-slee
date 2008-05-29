package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallInfoReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallInfoReportHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallInfoReportHelper.id(),"TpCallInfoReport",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallInfoType", org.csapi.cc.TpCallInfoTypeHelper.type(), null),new org.omg.CORBA.StructMember("CallInitiationStartTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallConnectedToResourceTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallConnectedToDestinationTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("CallEndTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("Cause", org.csapi.cc.TpReleaseCauseHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallInfoReport s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallInfoReport extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallInfoReport:1.0";
	}
	public static org.csapi.cc.TpCallInfoReport read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallInfoReport result = new org.csapi.cc.TpCallInfoReport();
		result.CallInfoType=in.read_long();
		result.CallInitiationStartTime=in.read_string();
		result.CallConnectedToResourceTime=in.read_string();
		result.CallConnectedToDestinationTime=in.read_string();
		result.CallEndTime=in.read_string();
		result.Cause=org.csapi.cc.TpReleaseCauseHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallInfoReport s)
	{
		out.write_long(s.CallInfoType);
		out.write_string(s.CallInitiationStartTime);
		out.write_string(s.CallConnectedToResourceTime);
		out.write_string(s.CallConnectedToDestinationTime);
		out.write_string(s.CallEndTime);
		org.csapi.cc.TpReleaseCauseHelper.write(out,s.Cause);
	}
}
