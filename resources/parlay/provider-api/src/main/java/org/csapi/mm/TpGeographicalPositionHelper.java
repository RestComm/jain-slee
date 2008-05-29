package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpGeographicalPosition"
 *	@author JacORB IDL compiler 
 */

public final class TpGeographicalPositionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpGeographicalPositionHelper.id(),"TpGeographicalPosition",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Longitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("Latitude", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("TypeOfUncertaintyShape", org.csapi.mm.TpLocationUncertaintyShapeHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyInnerSemiMajor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyOuterSemiMajor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyInnerSemiMinor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("UncertaintyOuterSemiMinor", org.csapi.TpFloatHelper.type(), null),new org.omg.CORBA.StructMember("AngleOfSemiMajor", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("SegmentStartAngle", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("SegmentEndAngle", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpGeographicalPosition s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpGeographicalPosition extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpGeographicalPosition:1.0";
	}
	public static org.csapi.mm.TpGeographicalPosition read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpGeographicalPosition result = new org.csapi.mm.TpGeographicalPosition();
		result.Longitude=in.read_float();
		result.Latitude=in.read_float();
		result.TypeOfUncertaintyShape=org.csapi.mm.TpLocationUncertaintyShapeHelper.read(in);
		result.UncertaintyInnerSemiMajor=in.read_float();
		result.UncertaintyOuterSemiMajor=in.read_float();
		result.UncertaintyInnerSemiMinor=in.read_float();
		result.UncertaintyOuterSemiMinor=in.read_float();
		result.AngleOfSemiMajor=in.read_long();
		result.SegmentStartAngle=in.read_long();
		result.SegmentEndAngle=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpGeographicalPosition s)
	{
		out.write_float(s.Longitude);
		out.write_float(s.Latitude);
		org.csapi.mm.TpLocationUncertaintyShapeHelper.write(out,s.TypeOfUncertaintyShape);
		out.write_float(s.UncertaintyInnerSemiMajor);
		out.write_float(s.UncertaintyOuterSemiMajor);
		out.write_float(s.UncertaintyInnerSemiMinor);
		out.write_float(s.UncertaintyOuterSemiMinor);
		out.write_long(s.AngleOfSemiMajor);
		out.write_long(s.SegmentStartAngle);
		out.write_long(s.SegmentEndAngle);
	}
}
