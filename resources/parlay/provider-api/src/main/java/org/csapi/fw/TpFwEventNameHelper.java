package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.fw.TpFwEventNameHelper.id(),"TpFwEventName",new String[]{"P_EVENT_FW_NAME_UNDEFINED","P_EVENT_FW_SERVICE_AVAILABLE","P_EVENT_FW_SERVICE_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.fw.TpFwEventName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.fw.TpFwEventName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/fw/TpFwEventName:1.0";
	}
	public static TpFwEventName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpFwEventName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpFwEventName s)
	{
		out.write_long(s.value());
	}
}
