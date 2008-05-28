package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionMonitorMode"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionMonitorModeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.dsc.TpDataSessionMonitorModeHelper.id(),"TpDataSessionMonitorMode",new String[]{"P_DATA_SESSION_MONITOR_MODE_INTERRUPT","P_DATA_SESSION_MONITOR_MODE_NOTIFY","P_DATA_SESSION_MONITOR_MODE_DO_NOT_MONITOR"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionMonitorMode s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionMonitorMode extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionMonitorMode:1.0";
	}
	public static TpDataSessionMonitorMode read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpDataSessionMonitorMode.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpDataSessionMonitorMode s)
	{
		out.write_long(s.value());
	}
}
