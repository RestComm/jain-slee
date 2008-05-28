package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIFault"
 *	@author JacORB IDL compiler 
 */

public final class TpUIFaultHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIFaultHelper.id(),"TpUIFault",new String[]{"P_UI_FAULT_UNDEFINED","P_UI_CALL_ENDED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIFault s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIFault extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIFault:1.0";
	}
	public static TpUIFault read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIFault.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIFault s)
	{
		out.write_long(s.value());
	}
}
