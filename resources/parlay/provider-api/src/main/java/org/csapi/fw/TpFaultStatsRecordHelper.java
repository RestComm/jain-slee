package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpFaultStatsRecord"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatsRecordHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpFaultStatsRecordHelper.id(),"TpFaultStatsRecord",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Period", org.csapi.TpTimeIntervalHelper.type(), null),new org.omg.CORBA.StructMember("FaultStatsSet", org.csapi.fw.TpFaultStatsSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFaultStatsRecord s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFaultStatsRecord extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFaultStatsRecord:1.0";
	}
	public static org.csapi.fw.TpFaultStatsRecord read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpFaultStatsRecord result = new org.csapi.fw.TpFaultStatsRecord();
		result.Period=org.csapi.TpTimeIntervalHelper.read(in);
		result.FaultStatsSet = org.csapi.fw.TpFaultStatsSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpFaultStatsRecord s)
	{
		org.csapi.TpTimeIntervalHelper.write(out,s.Period);
		org.csapi.fw.TpFaultStatsSetHelper.write(out,s.FaultStatsSet);
	}
}
