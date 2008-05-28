package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityError"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpMobilityErrorHelper.id(),"TpMobilityError",new String[]{"P_M_OK","P_M_SYSTEM_FAILURE","P_M_UNAUTHORIZED_NETWORK","P_M_UNAUTHORIZED_APPLICATION","P_M_UNKNOWN_SUBSCRIBER","P_M_ABSENT_SUBSCRIBER","P_M_POSITION_METHOD_FAILURE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpMobilityError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpMobilityError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpMobilityError:1.0";
	}
	public static TpMobilityError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpMobilityError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpMobilityError s)
	{
		out.write_long(s.value());
	}
}
