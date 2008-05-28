package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpResourceReservation"
 *	@author JacORB IDL compiler 
 */

public final class TpResourceReservationHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpResourceReservationHelper.id(),"TpResourceReservation",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ResourceID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("ReservationID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpResourceReservation s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpResourceReservation extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpResourceReservation:1.0";
	}
	public static org.csapi.cc.cccs.TpResourceReservation read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpResourceReservation result = new org.csapi.cc.cccs.TpResourceReservation();
		result.ResourceID=org.csapi.TpAddressHelper.read(in);
		result.ReservationID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpResourceReservation s)
	{
		org.csapi.TpAddressHelper.write(out,s.ResourceID);
		out.write_long(s.ReservationID);
	}
}
