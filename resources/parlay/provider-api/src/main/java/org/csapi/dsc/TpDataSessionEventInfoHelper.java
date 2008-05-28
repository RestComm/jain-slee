package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionEventInfoHelper.id(),"TpDataSessionEventInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("DataSessionEventName", org.csapi.dsc.TpDataSessionEventNameHelper.type(), null),new org.omg.CORBA.StructMember("MonitorMode", org.csapi.dsc.TpDataSessionMonitorModeHelper.type(), null),new org.omg.CORBA.StructMember("QoSClass", org.csapi.TpDataSessionQosClassHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionEventInfo:1.0";
	}
	public static org.csapi.dsc.TpDataSessionEventInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionEventInfo result = new org.csapi.dsc.TpDataSessionEventInfo();
		result.DestinationAddress=org.csapi.TpAddressHelper.read(in);
		result.OriginatingAddress=org.csapi.TpAddressHelper.read(in);
		result.DataSessionEventName=in.read_long();
		result.MonitorMode=org.csapi.dsc.TpDataSessionMonitorModeHelper.read(in);
		result.QoSClass=org.csapi.TpDataSessionQosClassHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionEventInfo s)
	{
		org.csapi.TpAddressHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressHelper.write(out,s.OriginatingAddress);
		out.write_long(s.DataSessionEventName);
		org.csapi.dsc.TpDataSessionMonitorModeHelper.write(out,s.MonitorMode);
		org.csapi.TpDataSessionQosClassHelper.write(out,s.QoSClass);
	}
}
