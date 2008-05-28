package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpBalanceQueryError"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceQueryErrorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.am.TpBalanceQueryErrorHelper.id(),"TpBalanceQueryError",new String[]{"P_BALANCE_QUERY_OK","P_BALANCE_QUERY_ERROR_UNDEFINED","P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER","P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION","P_BALANCE_QUERY_SYSTEM_FAILURE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpBalanceQueryError s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpBalanceQueryError extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpBalanceQueryError:1.0";
	}
	public static TpBalanceQueryError read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpBalanceQueryError.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpBalanceQueryError s)
	{
		out.write_long(s.value());
	}
}
