package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIVariablePartType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariablePartTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUIVariablePartTypeHelper.id(),"TpUIVariablePartType",new String[]{"P_UI_VARIABLE_PART_INT","P_UI_VARIABLE_PART_ADDRESS","P_UI_VARIABLE_PART_TIME","P_UI_VARIABLE_PART_DATE","P_UI_VARIABLE_PART_PRICE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIVariablePartType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIVariablePartType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIVariablePartType:1.0";
	}
	public static TpUIVariablePartType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUIVariablePartType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUIVariablePartType s)
	{
		out.write_long(s.value());
	}
}
