package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityStopScope"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopScopeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpMobilityStopScopeHelper.id(),"TpMobilityStopScope",new String[]{"P_M_ALL_IN_ASSIGNMENT","P_M_SPECIFIED_USERS"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpMobilityStopScope s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpMobilityStopScope extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpMobilityStopScope:1.0";
	}
	public static TpMobilityStopScope read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMobilityStopScope.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMobilityStopScope s)
	{
		out.write_long(s.value());
	}
}
