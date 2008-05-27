package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpChargingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpChargingEventInfoHelper.id(),"TpChargingEventInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargingEventName", org.csapi.am.TpChargingEventNameHelper.type(), null),new org.omg.CORBA.StructMember("CurrentBalanceInfo", org.csapi.am.TpBalanceInfoHelper.type(), null),new org.omg.CORBA.StructMember("ChargingEventTime", org.csapi.TpTimeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpChargingEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpChargingEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpChargingEventInfo:1.0";
	}
	public static org.csapi.am.TpChargingEventInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpChargingEventInfo result = new org.csapi.am.TpChargingEventInfo();
		result.ChargingEventName=org.csapi.am.TpChargingEventNameHelper.read(in);
		result.CurrentBalanceInfo=org.csapi.am.TpBalanceInfoHelper.read(in);
		result.ChargingEventTime=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpChargingEventInfo s)
	{
		org.csapi.am.TpChargingEventNameHelper.write(out,s.ChargingEventName);
		org.csapi.am.TpBalanceInfoHelper.write(out,s.CurrentBalanceInfo);
		out.write_string(s.ChargingEventTime);
	}
}
