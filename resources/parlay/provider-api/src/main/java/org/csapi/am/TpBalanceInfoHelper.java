package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpBalanceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpBalanceInfoHelper.id(),"TpBalanceInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Currency", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("ValuePartA", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("ValuePartB", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("Exponent", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("AdditionalInfo", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpBalanceInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpBalanceInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpBalanceInfo:1.0";
	}
	public static org.csapi.am.TpBalanceInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpBalanceInfo result = new org.csapi.am.TpBalanceInfo();
		result.Currency=in.read_string();
		result.ValuePartA=in.read_long();
		result.ValuePartB=in.read_long();
		result.Exponent=in.read_long();
		result.AdditionalInfo=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpBalanceInfo s)
	{
		out.write_string(s.Currency);
		out.write_long(s.ValuePartA);
		out.write_long(s.ValuePartB);
		out.write_long(s.Exponent);
		out.write_string(s.AdditionalInfo);
	}
}
