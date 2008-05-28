package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFaultStatisticsError"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatisticsErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpFaultStatisticsErrorHelper.id(),"TpFaultStatisticsError",new String[]{"P_FAULT_INFO_ERROR_UNDEFINED","P_FAULT_INFO_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFaultStatisticsError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFaultStatisticsError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFaultStatisticsError:1.0";
	}
	public static TpFaultStatisticsError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpFaultStatisticsError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpFaultStatisticsError s)
	{
		out.write_long(s.value());
	}
}
