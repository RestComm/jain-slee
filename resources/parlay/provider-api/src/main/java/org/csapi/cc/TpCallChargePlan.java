package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpCallChargePlan
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallChargePlan(){}
	public org.csapi.cc.TpCallChargeOrderCategory ChargeOrderType;
	public byte[] TransparentCharge;
	public int ChargePlan;
	public byte[] AdditionalInfo;
	public org.csapi.cc.TpCallPartyToChargeType PartyToCharge;
	public org.csapi.cc.TpCallPartyToChargeAdditionalInfo PartyToChargeAdditionalInfo;
	public TpCallChargePlan(org.csapi.cc.TpCallChargeOrderCategory ChargeOrderType, byte[] TransparentCharge, int ChargePlan, byte[] AdditionalInfo, org.csapi.cc.TpCallPartyToChargeType PartyToCharge, org.csapi.cc.TpCallPartyToChargeAdditionalInfo PartyToChargeAdditionalInfo)
	{
		this.ChargeOrderType = ChargeOrderType;
		this.TransparentCharge = TransparentCharge;
		this.ChargePlan = ChargePlan;
		this.AdditionalInfo = AdditionalInfo;
		this.PartyToCharge = PartyToCharge;
		this.PartyToChargeAdditionalInfo = PartyToChargeAdditionalInfo;
	}
}
