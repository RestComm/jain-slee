package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUlExtendedData"
 *	@author JacORB IDL compiler 
 */

public final class TpUlExtendedDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUlExtendedDataHelper.id(),"TpUlExtendedData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("GeographicalPosition", org.csapi.mm.TpGeographicalPositionHelper.type(), null),new org.omg.CORBA.StructMember("TerminalType", org.csapi.mm.TpTerminalTypeHelper.type(), null),new org.omg.CORBA.StructMember("AltitudePresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Altitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyAltitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("TimestampPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Timestamp", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("UsedLocationMethod", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUlExtendedData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUlExtendedData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUlExtendedData:1.0";
	}
	public static org.csapi.mm.TpUlExtendedData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUlExtendedData result = new org.csapi.mm.TpUlExtendedData();
		result.GeographicalPosition=org.csapi.mm.TpGeographicalPositionHelper.read(in);
		result.TerminalType=org.csapi.mm.TpTerminalTypeHelper.read(in);
		result.AltitudePresent=in.read_boolean();
		result.Altitude=in.read_float();
		result.UncertaintyAltitude=in.read_float();
		result.TimestampPresent=in.read_boolean();
		result.Timestamp=in.read_string();
		result.UsedLocationMethod=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUlExtendedData s)
	{
		org.csapi.mm.TpGeographicalPositionHelper.write(out,s.GeographicalPosition);
		org.csapi.mm.TpTerminalTypeHelper.write(out,s.TerminalType);
		out.write_boolean(s.AltitudePresent);
		out.write_float(s.Altitude);
		out.write_float(s.UncertaintyAltitude);
		out.write_boolean(s.TimestampPresent);
		out.write_string(s.Timestamp);
		out.write_string(s.UsedLocationMethod);
	}
}
