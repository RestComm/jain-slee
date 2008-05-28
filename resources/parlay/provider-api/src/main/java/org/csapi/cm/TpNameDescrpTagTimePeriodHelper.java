package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpNameDescrpTagTimePeriod"
 *	@author JacORB IDL compiler 
 */

public final class TpNameDescrpTagTimePeriodHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpNameDescrpTagTimePeriodHelper.id(),"TpNameDescrpTagTimePeriod",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("duration", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpNameDescrpTagTimePeriod s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpNameDescrpTagTimePeriod extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpNameDescrpTagTimePeriod:1.0";
	}
	public static org.csapi.cm.TpNameDescrpTagTimePeriod read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpNameDescrpTagTimePeriod result = new org.csapi.cm.TpNameDescrpTagTimePeriod();
		result.duration=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpNameDescrpTagTimePeriod s)
	{
		out.write_long(s.duration);
	}
}
