package org.csapi.mm;


/**
 *	Generated from IDL definition of struct "TpLocationResponseTime"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseTimeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.mm.TpLocationResponseTimeHelper.id(),"TpLocationResponseTime",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ResponseTime", org.csapi.mm.TpLocationResponseIndicatorHelper.type(), null),new org.omg.CORBA.StructMember("TimerValue", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationResponseTime s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationResponseTime extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationResponseTime:1.0";
	}
	public static org.csapi.mm.TpLocationResponseTime read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.mm.TpLocationResponseTime result = new org.csapi.mm.TpLocationResponseTime();
		result.ResponseTime=org.csapi.mm.TpLocationResponseIndicatorHelper.read(in);
		result.TimerValue=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.mm.TpLocationResponseTime s)
	{
		org.csapi.mm.TpLocationResponseIndicatorHelper.write(out,s.ResponseTime);
		out.write_long(s.TimerValue);
	}
}
