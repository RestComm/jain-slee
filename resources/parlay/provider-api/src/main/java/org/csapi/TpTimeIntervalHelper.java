package org.csapi;


/**
 *	Generated from IDL definition of struct "TpTimeInterval"
 *	@author JacORB IDL compiler 
 */

public final class TpTimeIntervalHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.TpTimeIntervalHelper.id(),"TpTimeInterval",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("StartTime", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("StopTime", org.csapi.TpDateAndTimeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpTimeInterval s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpTimeInterval extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpTimeInterval:1.0";
	}
	public static org.csapi.TpTimeInterval read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.TpTimeInterval result = new org.csapi.TpTimeInterval();
		result.StartTime=in.read_string();
		result.StopTime=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.TpTimeInterval s)
	{
		out.write_string(s.StartTime);
		out.write_string(s.StopTime);
	}
}
