package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallEventCriteriaHelper.id(),"TpCallEventCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("CallEventName", org.csapi.cc.gccs.TpCallEventNameHelper.type(), null),new org.omg.CORBA.StructMember("CallNotificationType", org.csapi.cc.gccs.TpCallNotificationTypeHelper.type(), null),new org.omg.CORBA.StructMember("MonitorMode", org.csapi.cc.TpCallMonitorModeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallEventCriteria:1.0";
	}
	public static org.csapi.cc.gccs.TpCallEventCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallEventCriteria result = new org.csapi.cc.gccs.TpCallEventCriteria();
		result.DestinationAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.OriginatingAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.CallEventName=in.read_long();
		result.CallNotificationType=org.csapi.cc.gccs.TpCallNotificationTypeHelper.read(in);
		result.MonitorMode=org.csapi.cc.TpCallMonitorModeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallEventCriteria s)
	{
		org.csapi.TpAddressRangeHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressRangeHelper.write(out,s.OriginatingAddress);
		out.write_long(s.CallEventName);
		org.csapi.cc.gccs.TpCallNotificationTypeHelper.write(out,s.CallNotificationType);
		org.csapi.cc.TpCallMonitorModeHelper.write(out,s.MonitorMode);
	}
}
