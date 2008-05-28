package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpLocationTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTriggerHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpLocationTriggerHelper.id(),"TpLocationTrigger",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Longitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("Latitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("AreaSemiMajor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("AreaSemiMinor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("AngleOfSemiMajor", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Criterion", org.csapi.mm.TpLocationTriggerCriteriaHelper.type(), null),new org.omg.CORBA.StructMember("ReportingInterval", org.csapi.TpDurationHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationTrigger s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationTrigger extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationTrigger:1.0";
	}
	public static org.csapi.mm.TpLocationTrigger read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpLocationTrigger result = new org.csapi.mm.TpLocationTrigger();
		result.Longitude=in.read_float();
		result.Latitude=in.read_float();
		result.AreaSemiMajor=in.read_float();
		result.AreaSemiMinor=in.read_float();
		result.AngleOfSemiMajor=in.read_long();
		result.Criterion=org.csapi.mm.TpLocationTriggerCriteriaHelper.read(in);
		result.ReportingInterval=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpLocationTrigger s)
	{
		out.write_float(s.Longitude);
		out.write_float(s.Latitude);
		out.write_float(s.AreaSemiMajor);
		out.write_float(s.AreaSemiMinor);
		out.write_long(s.AngleOfSemiMajor);
		org.csapi.mm.TpLocationTriggerCriteriaHelper.write(out,s.Criterion);
		out.write_long(s.ReportingInterval);
	}
}
