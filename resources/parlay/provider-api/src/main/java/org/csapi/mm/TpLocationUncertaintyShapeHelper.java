package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationUncertaintyShape"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationUncertaintyShapeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpLocationUncertaintyShapeHelper.id(),"TpLocationUncertaintyShape",new String[]{"P_M_SHAPE_NONE","P_M_SHAPE_CIRCLE","P_M_SHAPE_CIRCLE_SECTOR","P_M_SHAPE_CIRCLE_ARC_STRIPE","P_M_SHAPE_ELLIPSE","P_M_SHAPE_ELLIPSE_SECTOR","P_M_SHAPE_ELLIPSE_ARC_STRIPE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationUncertaintyShape s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationUncertaintyShape extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationUncertaintyShape:1.0";
	}
	public static TpLocationUncertaintyShape read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLocationUncertaintyShape.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLocationUncertaintyShape s)
	{
		out.write_long(s.value());
	}
}
