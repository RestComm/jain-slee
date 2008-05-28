package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticEntityType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpLoadStatisticEntityTypeHelper.id(),"TpLoadStatisticEntityType",new String[]{"P_LOAD_STATISTICS_FW_TYPE","P_LOAD_STATISTICS_SVC_TYPE","P_LOAD_STATISTICS_APP_TYPE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpLoadStatisticEntityType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpLoadStatisticEntityType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpLoadStatisticEntityType:1.0";
	}
	public static TpLoadStatisticEntityType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLoadStatisticEntityType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLoadStatisticEntityType s)
	{
		out.write_long(s.value());
	}
}
