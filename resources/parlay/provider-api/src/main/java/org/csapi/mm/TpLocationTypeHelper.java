package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationType"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpLocationTypeHelper.id(),"TpLocationType",new String[]{"P_M_CURRENT","P_M_CURRENT_OR_LAST_KNOWN","P_M_INITIAL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpLocationType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpLocationType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpLocationType:1.0";
	}
	public static TpLocationType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpLocationType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpLocationType s)
	{
		out.write_long(s.value());
	}
}
