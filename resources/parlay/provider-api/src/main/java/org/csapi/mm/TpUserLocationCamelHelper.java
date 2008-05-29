package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUserLocationCamel"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationCamelHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUserLocationCamelHelper.id(),"TpUserLocationCamel",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UserID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("StatusCode", org.csapi.mm.TpMobilityErrorHelper.type(), null),new org.omg.CORBA.StructMember("GeographicalPositionPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("GeographicalPosition", org.csapi.mm.TpGeographicalPositionHelper.type(), null),new org.omg.CORBA.StructMember("TimestampPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Timestamp", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("VlrNumberPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("VlrNumber", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("LocationNumberPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("LocationNumber", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("CellIdOrLaiPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("CellIdOrLai", org.csapi.mm.TpLocationCellIDOrLAIHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserLocationCamel s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserLocationCamel extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserLocationCamel:1.0";
	}
	public static org.csapi.mm.TpUserLocationCamel read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUserLocationCamel result = new org.csapi.mm.TpUserLocationCamel();
		result.UserID=org.csapi.TpAddressHelper.read(in);
		result.StatusCode=org.csapi.mm.TpMobilityErrorHelper.read(in);
		result.GeographicalPositionPresent=in.read_boolean();
		result.GeographicalPosition=org.csapi.mm.TpGeographicalPositionHelper.read(in);
		result.TimestampPresent=in.read_boolean();
		result.Timestamp=in.read_string();
		result.VlrNumberPresent=in.read_boolean();
		result.VlrNumber=org.csapi.TpAddressHelper.read(in);
		result.LocationNumberPresent=in.read_boolean();
		result.LocationNumber=org.csapi.TpAddressHelper.read(in);
		result.CellIdOrLaiPresent=in.read_boolean();
		result.CellIdOrLai=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUserLocationCamel s)
	{
		org.csapi.TpAddressHelper.write(out,s.UserID);
		org.csapi.mm.TpMobilityErrorHelper.write(out,s.StatusCode);
		out.write_boolean(s.GeographicalPositionPresent);
		org.csapi.mm.TpGeographicalPositionHelper.write(out,s.GeographicalPosition);
		out.write_boolean(s.TimestampPresent);
		out.write_string(s.Timestamp);
		out.write_boolean(s.VlrNumberPresent);
		org.csapi.TpAddressHelper.write(out,s.VlrNumber);
		out.write_boolean(s.LocationNumberPresent);
		org.csapi.TpAddressHelper.write(out,s.LocationNumber);
		out.write_boolean(s.CellIdOrLaiPresent);
		out.write_string(s.CellIdOrLai);
	}
}
