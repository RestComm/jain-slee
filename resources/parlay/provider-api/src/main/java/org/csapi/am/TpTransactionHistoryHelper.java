package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpTransactionHistory"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistoryHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpTransactionHistoryHelper.id(),"TpTransactionHistory",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("TransactionID", org.csapi.TpAssignmentIDHelper.type(), null),new org.omg.CORBA.StructMember("TimeStamp", org.csapi.TpDateAndTimeHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalInfo", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpTransactionHistory s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpTransactionHistory extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpTransactionHistory:1.0";
	}
	public static org.csapi.am.TpTransactionHistory read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpTransactionHistory result = new org.csapi.am.TpTransactionHistory();
		result.TransactionID=in.read_long();
		result.TimeStamp=in.read_string();
		result.AdditionalInfo=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpTransactionHistory s)
	{
		out.write_long(s.TransactionID);
		out.write_string(s.TimeStamp);
		out.write_string(s.AdditionalInfo);
	}
}
