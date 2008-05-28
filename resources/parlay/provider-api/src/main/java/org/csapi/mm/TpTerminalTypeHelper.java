package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpTerminalType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpTerminalTypeHelper.id(),"TpTerminalType",new String[]{"P_M_FIXED","P_M_MOBILE","P_M_IP"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpTerminalType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpTerminalType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpTerminalType:1.0";
	}
	public static TpTerminalType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpTerminalType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpTerminalType s)
	{
		out.write_long(s.value());
	}
}
