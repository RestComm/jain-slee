package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpFaultStats"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpFaultStatsHelper.id(),"TpFaultStats",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Fault", org.csapi.fw.TpInterfaceFaultHelper.type(), null),new org.omg.CORBA.StructMember("Occurrences", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("MaxDuration", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("TotalDuration", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("NumberOfClientsAffected", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFaultStats s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFaultStats extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFaultStats:1.0";
	}
	public static org.csapi.fw.TpFaultStats read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpFaultStats result = new org.csapi.fw.TpFaultStats();
		result.Fault=org.csapi.fw.TpInterfaceFaultHelper.read(in);
		result.Occurrences=in.read_long();
		result.MaxDuration=in.read_long();
		result.TotalDuration=in.read_long();
		result.NumberOfClientsAffected=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpFaultStats s)
	{
		org.csapi.fw.TpInterfaceFaultHelper.write(out,s.Fault);
		out.write_long(s.Occurrences);
		out.write_long(s.MaxDuration);
		out.write_long(s.TotalDuration);
		out.write_long(s.NumberOfClientsAffected);
	}
}
