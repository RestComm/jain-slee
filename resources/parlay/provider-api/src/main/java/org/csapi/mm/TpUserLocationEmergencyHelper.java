package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUserLocationEmergency"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUserLocationEmergencyHelper.id(),"TpUserLocationEmergency",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("StatusCode", org.csapi.mm.TpMobilityErrorHelper.type(), null),new org.omg.CORBA.StructMember("UserIdPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("UserId", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrdPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrd", org.csapi.mm.TpNaESRDHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrkPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrk", org.csapi.mm.TpNaESRKHelper.type(), null),new org.omg.CORBA.StructMember("ImeiPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Imei", org.csapi.mm.TpIMEIHelper.type(), null),new org.omg.CORBA.StructMember("TriggeringEvent", org.csapi.mm.TpUserLocationEmergencyTriggerHelper.type(), null),new org.omg.CORBA.StructMember("GeographicalPositionPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("GeographicalPosition", org.csapi.mm.TpGeographicalPositionHelper.type(), null),new org.omg.CORBA.StructMember("AltitudePresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Altitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyAltitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("TimestampPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Timestamp", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("UsedLocationMethod", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserLocationEmergency s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserLocationEmergency extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserLocationEmergency:1.0";
	}
	public static org.csapi.mm.TpUserLocationEmergency read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUserLocationEmergency result = new org.csapi.mm.TpUserLocationEmergency();
		result.StatusCode=org.csapi.mm.TpMobilityErrorHelper.read(in);
		result.UserIdPresent=in.read_boolean();
		result.UserId=org.csapi.TpAddressHelper.read(in);
		result.NaEsrdPresent=in.read_boolean();
		result.NaEsrd=in.read_string();
		result.NaEsrkPresent=in.read_boolean();
		result.NaEsrk=in.read_string();
		result.ImeiPresent=in.read_boolean();
		result.Imei=in.read_string();
		result.TriggeringEvent=org.csapi.mm.TpUserLocationEmergencyTriggerHelper.read(in);
		result.GeographicalPositionPresent=in.read_boolean();
		result.GeographicalPosition=org.csapi.mm.TpGeographicalPositionHelper.read(in);
		result.AltitudePresent=in.read_boolean();
		result.Altitude=in.read_float();
		result.UncertaintyAltitude=in.read_float();
		result.TimestampPresent=in.read_boolean();
		result.Timestamp=in.read_string();
		result.UsedLocationMethod=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUserLocationEmergency s)
	{
		org.csapi.mm.TpMobilityErrorHelper.write(out,s.StatusCode);
		out.write_boolean(s.UserIdPresent);
		org.csapi.TpAddressHelper.write(out,s.UserId);
		out.write_boolean(s.NaEsrdPresent);
		out.write_string(s.NaEsrd);
		out.write_boolean(s.NaEsrkPresent);
		out.write_string(s.NaEsrk);
		out.write_boolean(s.ImeiPresent);
		out.write_string(s.Imei);
		org.csapi.mm.TpUserLocationEmergencyTriggerHelper.write(out,s.TriggeringEvent);
		out.write_boolean(s.GeographicalPositionPresent);
		org.csapi.mm.TpGeographicalPositionHelper.write(out,s.GeographicalPosition);
		out.write_boolean(s.AltitudePresent);
		out.write_float(s.Altitude);
		out.write_float(s.UncertaintyAltitude);
		out.write_boolean(s.TimestampPresent);
		out.write_string(s.Timestamp);
		out.write_string(s.UsedLocationMethod);
	}
}
