package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpChargingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpChargingEventCriteriaHelper.id(),"TpChargingEventCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargingEvents", org.csapi.am.TpChargingEventNameSetHelper.type(), null),new org.omg.CORBA.StructMember("Users", org.csapi.TpAddressSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpChargingEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpChargingEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpChargingEventCriteria:1.0";
	}
	public static org.csapi.am.TpChargingEventCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpChargingEventCriteria result = new org.csapi.am.TpChargingEventCriteria();
		result.ChargingEvents = org.csapi.am.TpChargingEventNameSetHelper.read(in);
		result.Users = org.csapi.TpAddressSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpChargingEventCriteria s)
	{
		org.csapi.am.TpChargingEventNameSetHelper.write(out,s.ChargingEvents);
		org.csapi.TpAddressSetHelper.write(out,s.Users);
	}
}
