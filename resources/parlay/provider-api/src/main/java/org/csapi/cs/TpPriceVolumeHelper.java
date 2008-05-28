package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpPriceVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpPriceVolumeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpPriceVolumeHelper.id(),"TpPriceVolume",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Price", org.csapi.cs.TpChargingPriceHelper.type(), null),new org.omg.CORBA.StructMember("Volume", org.csapi.cs.TpVolumeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpPriceVolume s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpPriceVolume extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpPriceVolume:1.0";
	}
	public static org.csapi.cs.TpPriceVolume read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpPriceVolume result = new org.csapi.cs.TpPriceVolume();
		result.Price=org.csapi.cs.TpChargingPriceHelper.read(in);
		result.Volume=org.csapi.cs.TpVolumeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpPriceVolume s)
	{
		org.csapi.cs.TpChargingPriceHelper.write(out,s.Price);
		org.csapi.cs.TpVolumeHelper.write(out,s.Volume);
	}
}
