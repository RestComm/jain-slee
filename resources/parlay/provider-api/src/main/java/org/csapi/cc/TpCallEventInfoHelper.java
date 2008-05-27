package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallEventInfoHelper.id(),"TpCallEventInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallEventType", org.csapi.cc.TpCallEventTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalCallEventInfo", org.csapi.cc.TpCallAdditionalEventInfoHelper.type(), null),new org.omg.CORBA.StructMember("CallMonitorMode", org.csapi.cc.TpCallMonitorModeHelper.type(), null),new org.omg.CORBA.StructMember("CallEventTime", org.csapi.TpDateAndTimeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallEventInfo:1.0";
	}
	public static org.csapi.cc.TpCallEventInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallEventInfo result = new org.csapi.cc.TpCallEventInfo();
		result.CallEventType=org.csapi.cc.TpCallEventTypeHelper.read(in);
		result.AdditionalCallEventInfo=org.csapi.cc.TpCallAdditionalEventInfoHelper.read(in);
		result.CallMonitorMode=org.csapi.cc.TpCallMonitorModeHelper.read(in);
		result.CallEventTime=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallEventInfo s)
	{
		org.csapi.cc.TpCallEventTypeHelper.write(out,s.CallEventType);
		org.csapi.cc.TpCallAdditionalEventInfoHelper.write(out,s.AdditionalCallEventInfo);
		org.csapi.cc.TpCallMonitorModeHelper.write(out,s.CallMonitorMode);
		out.write_string(s.CallEventTime);
	}
}
