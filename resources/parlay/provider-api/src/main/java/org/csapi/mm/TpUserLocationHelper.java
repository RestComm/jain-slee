package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpUserLocation"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpUserLocationHelper.id(),"TpUserLocation",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UserID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("StatusCode", org.csapi.mm.TpMobilityErrorHelper.type(), null),new org.omg.CORBA.StructMember("GeographicalPosition", org.csapi.mm.TpGeographicalPositionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserLocation s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserLocation extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserLocation:1.0";
	}
	public static org.csapi.mm.TpUserLocation read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpUserLocation result = new org.csapi.mm.TpUserLocation();
		result.UserID=org.csapi.TpAddressHelper.read(in);
		result.StatusCode=org.csapi.mm.TpMobilityErrorHelper.read(in);
		result.GeographicalPosition=org.csapi.mm.TpGeographicalPositionHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpUserLocation s)
	{
		org.csapi.TpAddressHelper.write(out,s.UserID);
		org.csapi.mm.TpMobilityErrorHelper.write(out,s.StatusCode);
		org.csapi.mm.TpGeographicalPositionHelper.write(out,s.GeographicalPosition);
	}
}
