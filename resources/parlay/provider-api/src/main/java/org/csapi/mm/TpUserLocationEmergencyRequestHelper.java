package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUserLocationEmergencyRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUserLocationEmergencyRequestHelper.id(),"TpUserLocationEmergencyRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UserAddressPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("UserAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrdPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrd", org.csapi.mm.TpNaESRDHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrkPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("NaEsrk", org.csapi.mm.TpNaESRKHelper.type(), null),new org.omg.CORBA.StructMember("ImeiPresent", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Imei", org.csapi.mm.TpIMEIHelper.type(), null),new org.omg.CORBA.StructMember("LocationReq", org.csapi.mm.TpLocationRequestHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserLocationEmergencyRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserLocationEmergencyRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserLocationEmergencyRequest:1.0";
	}
	public static org.csapi.mm.TpUserLocationEmergencyRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUserLocationEmergencyRequest result = new org.csapi.mm.TpUserLocationEmergencyRequest();
		result.UserAddressPresent=in.read_boolean();
		result.UserAddress=org.csapi.TpAddressHelper.read(in);
		result.NaEsrdPresent=in.read_boolean();
		result.NaEsrd=in.read_string();
		result.NaEsrkPresent=in.read_boolean();
		result.NaEsrk=in.read_string();
		result.ImeiPresent=in.read_boolean();
		result.Imei=in.read_string();
		result.LocationReq=org.csapi.mm.TpLocationRequestHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUserLocationEmergencyRequest s)
	{
		out.write_boolean(s.UserAddressPresent);
		org.csapi.TpAddressHelper.write(out,s.UserAddress);
		out.write_boolean(s.NaEsrdPresent);
		out.write_string(s.NaEsrd);
		out.write_boolean(s.NaEsrkPresent);
		out.write_string(s.NaEsrk);
		out.write_boolean(s.ImeiPresent);
		out.write_string(s.Imei);
		org.csapi.mm.TpLocationRequestHelper.write(out,s.LocationReq);
	}
}
