package org.csapi.termcap;
/**
 *	Generated from IDL definition of enum "TpTerminalCapabilityScopeType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScopeTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.termcap.TpTerminalCapabilityScopeTypeHelper.id(),"TpTerminalCapabilityScopeType",new String[]{"P_TERMINAL_CAPABILITY_SCOPE_TYPE_UNDEFINED","P_TERMINAL_CAPABILITY_SCOPE_TYPE_CCPP"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.termcap.TpTerminalCapabilityScopeType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.termcap.TpTerminalCapabilityScopeType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/termcap/TpTerminalCapabilityScopeType:1.0";
	}
	public static TpTerminalCapabilityScopeType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpTerminalCapabilityScopeType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpTerminalCapabilityScopeType s)
	{
		out.write_long(s.value());
	}
}
