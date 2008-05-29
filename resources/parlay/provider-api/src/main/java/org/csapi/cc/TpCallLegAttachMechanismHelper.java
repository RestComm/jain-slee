package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallLegAttachMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegAttachMechanismHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallLegAttachMechanismHelper.id(),"TpCallLegAttachMechanism",new String[]{"P_CALLLEG_ATTACH_IMPLICITLY","P_CALLLEG_ATTACH_EXPLICITLY"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallLegAttachMechanism s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallLegAttachMechanism extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallLegAttachMechanism:1.0";
	}
	public static TpCallLegAttachMechanism read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallLegAttachMechanism.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallLegAttachMechanism s)
	{
		out.write_long(s.value());
	}
}
