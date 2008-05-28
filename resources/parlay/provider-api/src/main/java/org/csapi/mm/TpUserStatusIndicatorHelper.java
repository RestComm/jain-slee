package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserStatusIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusIndicatorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpUserStatusIndicatorHelper.id(),"TpUserStatusIndicator",new String[]{"P_US_REACHABLE","P_US_NOT_REACHABLE","P_US_BUSY"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserStatusIndicator s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserStatusIndicator extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserStatusIndicator:1.0";
	}
	public static TpUserStatusIndicator read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUserStatusIndicator.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUserStatusIndicator s)
	{
		out.write_long(s.value());
	}
}
