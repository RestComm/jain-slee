package org.csapi.dsc;


/**
 *	Generated from IDL definition of struct "TpDataSessionChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargePlanHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.dsc.TpDataSessionChargePlanHelper.id(),"TpDataSessionChargePlan",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargeOrderType", org.csapi.dsc.TpDataSessionChargeOrderHelper.type(), null),new org.omg.CORBA.StructMember("Currency", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalInfo", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.dsc.TpDataSessionChargePlan s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.dsc.TpDataSessionChargePlan extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/dsc/TpDataSessionChargePlan:1.0";
	}
	public static org.csapi.dsc.TpDataSessionChargePlan read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.dsc.TpDataSessionChargePlan result = new org.csapi.dsc.TpDataSessionChargePlan();
		result.ChargeOrderType=org.csapi.dsc.TpDataSessionChargeOrderHelper.read(in);
		result.Currency=in.read_string();
		result.AdditionalInfo=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.dsc.TpDataSessionChargePlan s)
	{
		org.csapi.dsc.TpDataSessionChargeOrderHelper.write(out,s.ChargeOrderType);
		out.write_string(s.Currency);
		out.write_string(s.AdditionalInfo);
	}
}
