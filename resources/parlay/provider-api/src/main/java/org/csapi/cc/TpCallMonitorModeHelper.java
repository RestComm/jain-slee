package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallMonitorMode"
 *	@author JacORB IDL compiler 
 */

public final class TpCallMonitorModeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallMonitorModeHelper.id(),"TpCallMonitorMode",new String[]{"P_CALL_MONITOR_MODE_INTERRUPT","P_CALL_MONITOR_MODE_NOTIFY","P_CALL_MONITOR_MODE_DO_NOT_MONITOR"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallMonitorMode s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallMonitorMode extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallMonitorMode:1.0";
	}
	public static TpCallMonitorMode read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallMonitorMode.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallMonitorMode s)
	{
		out.write_long(s.value());
	}
}
