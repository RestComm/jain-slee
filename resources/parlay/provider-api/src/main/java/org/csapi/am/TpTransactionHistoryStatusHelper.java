package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpTransactionHistoryStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistoryStatusHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.am.TpTransactionHistoryStatusHelper.id(),"TpTransactionHistoryStatus",new String[]{"P_AM_TRANSACTION_ERROR_UNSPECIFIED","P_AM_TRANSACTION_INVALID_INTERVAL","P_AM_TRANSACTION_UNKNOWN_ACCOUNT","P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION","P_AM_TRANSACTION_PROCESSING_ERROR","P_AM_TRANSACTION_SYSTEM_FAILURE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpTransactionHistoryStatus s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpTransactionHistoryStatus extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpTransactionHistoryStatus:1.0";
	}
	public static TpTransactionHistoryStatus read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpTransactionHistoryStatus.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpTransactionHistoryStatus s)
	{
		out.write_long(s.value());
	}
}
