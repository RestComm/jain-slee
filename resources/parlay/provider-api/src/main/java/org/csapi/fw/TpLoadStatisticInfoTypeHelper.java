package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfoTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpLoadStatisticInfoTypeHelper.id(),"TpLoadStatisticInfoType",new String[]{"P_LOAD_STATISTICS_VALID","P_LOAD_STATISTICS_INVALID"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatisticInfoType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatisticInfoType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatisticInfoType:1.0";
	}
	public static TpLoadStatisticInfoType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLoadStatisticInfoType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLoadStatisticInfoType s)
	{
		out.write_long(s.value());
	}
}
