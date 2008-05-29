package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilitiesError"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilitiesErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.termcap.TpTerminalCapabilitiesErrorHelper.id(),"TpTerminalCapabilitiesError",new String[]{"P_TERMCAP_ERROR_UNDEFINED","P_TERMCAP_INVALID_TERMINALID","P_TERMCAP_SYSTEM_FAILURE","P_TERMCAP_INFO_UNAVAILABLE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.termcap.TpTerminalCapabilitiesError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.termcap.TpTerminalCapabilitiesError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/termcap/TpTerminalCapabilitiesError:1.0";
	}
	public static TpTerminalCapabilitiesError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpTerminalCapabilitiesError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpTerminalCapabilitiesError s)
	{
		out.write_long(s.value());
	}
}
