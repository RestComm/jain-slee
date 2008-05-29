package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpCallSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpCallSuperviseVolumeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.id(),"TpCallSuperviseVolume",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("VolumeQuantity", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("VolumeUnit", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpCallSuperviseVolume s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpCallSuperviseVolume extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpCallSuperviseVolume:1.0";
	}
	public static org.csapi.cc.mmccs.TpCallSuperviseVolume read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpCallSuperviseVolume result = new org.csapi.cc.mmccs.TpCallSuperviseVolume();
		result.VolumeQuantity=in.read_long();
		result.VolumeUnit=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpCallSuperviseVolume s)
	{
		out.write_long(s.VolumeQuantity);
		out.write_long(s.VolumeUnit);
	}
}
