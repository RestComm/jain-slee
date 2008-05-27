package org.csapi.fw;


/**
 *	Generated from IDL definition of struct "TpLoadStatistic"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.fw.TpLoadStatisticHelper.id(),"TpLoadStatistic",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("LoadStatisticEntityID", org.csapi.fw.TpLoadStatisticEntityIDHelper.type(), null),new org.omg.CORBA.StructMember("TimeStamp", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("LoadStatisticInfo", org.csapi.fw.TpLoadStatisticInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatistic s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatistic extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatistic:1.0";
	}
	public static org.csapi.fw.TpLoadStatistic read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.fw.TpLoadStatistic result = new org.csapi.fw.TpLoadStatistic();
		result.LoadStatisticEntityID=org.csapi.fw.TpLoadStatisticEntityIDHelper.read(in);
		result.TimeStamp=in.read_string();
		result.LoadStatisticInfo=org.csapi.fw.TpLoadStatisticInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.fw.TpLoadStatistic s)
	{
		org.csapi.fw.TpLoadStatisticEntityIDHelper.write(out,s.LoadStatisticEntityID);
		out.write_string(s.TimeStamp);
		org.csapi.fw.TpLoadStatisticInfoHelper.write(out,s.LoadStatisticInfo);
	}
}
