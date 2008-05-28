package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpLocationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpLocationRequestHelper.id(),"TpLocationRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("RequestedAccuracy", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("RequestedResponseTime", org.csapi.mm.TpLocationResponseTimeHelper.type(), null),new org.omg.CORBA.StructMember("AltitudeRequested", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("Type", org.csapi.mm.TpLocationTypeHelper.type(), null),new org.omg.CORBA.StructMember("Priority", org.csapi.mm.TpLocationPriorityHelper.type(), null),new org.omg.CORBA.StructMember("RequestedLocationMethod", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationRequest:1.0";
	}
	public static org.csapi.mm.TpLocationRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpLocationRequest result = new org.csapi.mm.TpLocationRequest();
		result.RequestedAccuracy=in.read_float();
		result.RequestedResponseTime=org.csapi.mm.TpLocationResponseTimeHelper.read(in);
		result.AltitudeRequested=in.read_boolean();
		result.Type=org.csapi.mm.TpLocationTypeHelper.read(in);
		result.Priority=org.csapi.mm.TpLocationPriorityHelper.read(in);
		result.RequestedLocationMethod=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpLocationRequest s)
	{
		out.write_float(s.RequestedAccuracy);
		org.csapi.mm.TpLocationResponseTimeHelper.write(out,s.RequestedResponseTime);
		out.write_boolean(s.AltitudeRequested);
		org.csapi.mm.TpLocationTypeHelper.write(out,s.Type);
		org.csapi.mm.TpLocationPriorityHelper.write(out,s.Priority);
		out.write_string(s.RequestedLocationMethod);
	}
}
