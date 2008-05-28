package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUITargetObjectType"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObjectTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.ui.TpUITargetObjectTypeHelper.id(),"TpUITargetObjectType",new String[]{"P_UI_TARGET_OBJECT_CALL","P_UI_TARGET_OBJECT_MULTI_PARTY_CALL","P_UI_TARGET_OBJECT_CALL_LEG"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUITargetObjectType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUITargetObjectType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUITargetObjectType:1.0";
	}
	public static TpUITargetObjectType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUITargetObjectType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUITargetObjectType s)
	{
		out.write_long(s.value());
	}
}
