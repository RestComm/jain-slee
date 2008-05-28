package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationPriority"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationPriorityHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpLocationPriorityHelper.id(),"TpLocationPriority",new String[]{"P_M_NORMAL","P_M_HIGH"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationPriority s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationPriority extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationPriority:1.0";
	}
	public static TpLocationPriority read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLocationPriority.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLocationPriority s)
	{
		out.write_long(s.value());
	}
}
