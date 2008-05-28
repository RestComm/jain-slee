package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionSuperviseVolumeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.id(),"TpDataSessionSuperviseVolume",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("VolumeQuantity", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("VolumeUnit", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionSuperviseVolume s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionSuperviseVolume extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionSuperviseVolume:1.0";
	}
	public static org.csapi.dsc.TpDataSessionSuperviseVolume read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionSuperviseVolume result = new org.csapi.dsc.TpDataSessionSuperviseVolume();
		result.VolumeQuantity=in.read_long();
		result.VolumeUnit=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionSuperviseVolume s)
	{
		out.write_long(s.VolumeQuantity);
		out.write_long(s.VolumeUnit);
	}
}
