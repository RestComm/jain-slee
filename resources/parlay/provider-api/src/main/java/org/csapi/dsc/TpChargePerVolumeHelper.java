package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpChargePerVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerVolumeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpChargePerVolumeHelper.id(),"TpChargePerVolume",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("InitialCharge", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("CurrentChargePerKilobyte", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("NextChargePerKilobyte", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpChargePerVolume s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpChargePerVolume extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpChargePerVolume:1.0";
	}
	public static org.csapi.dsc.TpChargePerVolume read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpChargePerVolume result = new org.csapi.dsc.TpChargePerVolume();
		result.InitialCharge=in.read_long();
		result.CurrentChargePerKilobyte=in.read_long();
		result.NextChargePerKilobyte=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpChargePerVolume s)
	{
		out.write_long(s.InitialCharge);
		out.write_long(s.CurrentChargePerKilobyte);
		out.write_long(s.NextChargePerKilobyte);
	}
}
