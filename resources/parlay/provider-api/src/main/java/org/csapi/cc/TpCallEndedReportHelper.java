package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallEndedReport"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEndedReportHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallEndedReportHelper.id(),"TpCallEndedReport",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallLegSessionID", org.csapi.TpSessionIDHelper.type(), null),new org.omg.CORBA.StructMember("Cause", org.csapi.cc.TpReleaseCauseHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallEndedReport s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallEndedReport extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallEndedReport:1.0";
	}
	public static org.csapi.cc.TpCallEndedReport read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallEndedReport result = new org.csapi.cc.TpCallEndedReport();
		result.CallLegSessionID=in.read_long();
		result.Cause=org.csapi.cc.TpReleaseCauseHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallEndedReport s)
	{
		out.write_long(s.CallLegSessionID);
		org.csapi.cc.TpReleaseCauseHelper.write(out,s.Cause);
	}
}
