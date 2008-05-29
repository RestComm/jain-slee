package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallEventRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallEventRequestHelper.id(),"TpCallEventRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallEventType", org.csapi.cc.TpCallEventTypeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalCallEventCriteria", org.csapi.cc.TpAdditionalCallEventCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("CallMonitorMode", org.csapi.cc.TpCallMonitorModeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallEventRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallEventRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallEventRequest:1.0";
	}
	public static org.csapi.cc.TpCallEventRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallEventRequest result = new org.csapi.cc.TpCallEventRequest();
		result.CallEventType=org.csapi.cc.TpCallEventTypeHelper.read(in);
		result.AdditionalCallEventCriteria=org.csapi.cc.TpAdditionalCallEventCriteriaHelper.read(in);
		result.CallMonitorMode=org.csapi.cc.TpCallMonitorModeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallEventRequest s)
	{
		org.csapi.cc.TpCallEventTypeHelper.write(out,s.CallEventType);
		org.csapi.cc.TpAdditionalCallEventCriteriaHelper.write(out,s.AdditionalCallEventCriteria);
		org.csapi.cc.TpCallMonitorModeHelper.write(out,s.CallMonitorMode);
	}
}
