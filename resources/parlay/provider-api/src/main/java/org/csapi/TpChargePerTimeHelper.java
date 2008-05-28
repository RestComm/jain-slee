package org.csapi;


/**
 *	Generated from IDL definition of struct "TpChargePerTime"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerTimeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpChargePerTimeHelper.id(),"TpChargePerTime",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("InitialCharge", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("CurrentChargePerMinute", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("NextChargePerMinute", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpChargePerTime s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpChargePerTime extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpChargePerTime:1.0";
	}
	public static org.csapi.TpChargePerTime read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpChargePerTime result = new org.csapi.TpChargePerTime();
		result.InitialCharge=in.read_long();
		result.CurrentChargePerMinute=in.read_long();
		result.NextChargePerMinute=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpChargePerTime s)
	{
		out.write_long(s.InitialCharge);
		out.write_long(s.CurrentChargePerMinute);
		out.write_long(s.NextChargePerMinute);
	}
}
