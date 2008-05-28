package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpMerchantAccountID"
 *	@author JacORB IDL compiler 
 */

public final class TpMerchantAccountIDHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpMerchantAccountIDHelper.id(),"TpMerchantAccountID",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MerchantID", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("AccountID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpMerchantAccountID s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpMerchantAccountID extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpMerchantAccountID:1.0";
	}
	public static org.csapi.cs.TpMerchantAccountID read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpMerchantAccountID result = new org.csapi.cs.TpMerchantAccountID();
		result.MerchantID=in.read_string();
		result.AccountID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpMerchantAccountID s)
	{
		out.write_string(s.MerchantID);
		out.write_long(s.AccountID);
	}
}
