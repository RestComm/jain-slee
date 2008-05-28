package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionEventCriteriaHelper.id(),"TpDataSessionEventCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("OriginationAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("DataSessionEventName", org.csapi.dsc.TpDataSessionEventNameHelper.type(), null),new org.omg.CORBA.StructMember("MonitorMode", org.csapi.dsc.TpDataSessionMonitorModeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionEventCriteria:1.0";
	}
	public static org.csapi.dsc.TpDataSessionEventCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionEventCriteria result = new org.csapi.dsc.TpDataSessionEventCriteria();
		result.DestinationAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.OriginationAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.DataSessionEventName=in.read_long();
		result.MonitorMode=org.csapi.dsc.TpDataSessionMonitorModeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionEventCriteria s)
	{
		org.csapi.TpAddressRangeHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressRangeHelper.write(out,s.OriginationAddress);
		out.write_long(s.DataSessionEventName);
		org.csapi.dsc.TpDataSessionMonitorModeHelper.write(out,s.MonitorMode);
	}
}
