package org.csapi;


/**
 *	Generated from IDL definition of struct "TpCAIElements"
 *	@author JacORB IDL compiler 
 */

public final class TpCAIElementsHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpCAIElementsHelper.id(),"TpCAIElements",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UnitsPerInterval", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("SecondsPerTimeInterval", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("ScalingFactor", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("UnitIncrement", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("UnitsPerDataInterval", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("SegmentsPerDataInterval", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("InitialSecsPerTimeInterval", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpCAIElements s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpCAIElements extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpCAIElements:1.0";
	}
	public static org.csapi.TpCAIElements read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpCAIElements result = new org.csapi.TpCAIElements();
		result.UnitsPerInterval=in.read_long();
		result.SecondsPerTimeInterval=in.read_long();
		result.ScalingFactor=in.read_long();
		result.UnitIncrement=in.read_long();
		result.UnitsPerDataInterval=in.read_long();
		result.SegmentsPerDataInterval=in.read_long();
		result.InitialSecsPerTimeInterval=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpCAIElements s)
	{
		out.write_long(s.UnitsPerInterval);
		out.write_long(s.SecondsPerTimeInterval);
		out.write_long(s.ScalingFactor);
		out.write_long(s.UnitIncrement);
		out.write_long(s.UnitsPerDataInterval);
		out.write_long(s.SegmentsPerDataInterval);
		out.write_long(s.InitialSecsPerTimeInterval);
	}
}
