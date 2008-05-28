package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargePlanHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallChargePlanHelper.id(),"TpCallChargePlan",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargeOrderType", org.csapi.cc.TpCallChargeOrderCategoryHelper.type(), null),new org.omg.CORBA.StructMember("TransparentCharge", org.csapi.TpOctetSetHelper.type(), null),new org.omg.CORBA.StructMember("ChargePlan", org.csapi.TpInt32Helper.type(), null),new org.omg.CORBA.StructMember("AdditionalInfo", org.csapi.TpOctetSetHelper.type(), null),new org.omg.CORBA.StructMember("PartyToCharge", org.csapi.cc.TpCallPartyToChargeTypeHelper.type(), null),new org.omg.CORBA.StructMember("PartyToChargeAdditionalInfo", org.csapi.cc.TpCallPartyToChargeAdditionalInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallChargePlan s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallChargePlan extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallChargePlan:1.0";
	}
	public static org.csapi.cc.TpCallChargePlan read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallChargePlan result = new org.csapi.cc.TpCallChargePlan();
		result.ChargeOrderType=org.csapi.cc.TpCallChargeOrderCategoryHelper.read(in);
		result.TransparentCharge = org.csapi.TpOctetSetHelper.read(in);
		result.ChargePlan=in.read_long();
		result.AdditionalInfo = org.csapi.TpOctetSetHelper.read(in);
		result.PartyToCharge=org.csapi.cc.TpCallPartyToChargeTypeHelper.read(in);
		result.PartyToChargeAdditionalInfo=org.csapi.cc.TpCallPartyToChargeAdditionalInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallChargePlan s)
	{
		org.csapi.cc.TpCallChargeOrderCategoryHelper.write(out,s.ChargeOrderType);
		org.csapi.TpOctetSetHelper.write(out,s.TransparentCharge);
		out.write_long(s.ChargePlan);
		org.csapi.TpOctetSetHelper.write(out,s.AdditionalInfo);
		org.csapi.cc.TpCallPartyToChargeTypeHelper.write(out,s.PartyToCharge);
		org.csapi.cc.TpCallPartyToChargeAdditionalInfoHelper.write(out,s.PartyToChargeAdditionalInfo);
	}
}
