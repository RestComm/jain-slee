package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpChargingPrice"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingPriceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpChargingPriceHelper.id(),"TpChargingPrice",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Currency", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("Amount", org.csapi.cs.TpAmountHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingPrice s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingPrice extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingPrice:1.0";
	}
	public static org.csapi.cs.TpChargingPrice read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpChargingPrice result = new org.csapi.cs.TpChargingPrice();
		result.Currency=in.read_string();
		result.Amount=org.csapi.cs.TpAmountHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpChargingPrice s)
	{
		out.write_string(s.Currency);
		org.csapi.cs.TpAmountHelper.write(out,s.Amount);
	}
}
