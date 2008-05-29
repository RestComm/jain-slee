package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionChargePlan"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargePlan
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionChargePlan(){}
	public org.csapi.dsc.TpDataSessionChargeOrder ChargeOrderType;
	public java.lang.String Currency;
	public java.lang.String AdditionalInfo;
	public TpDataSessionChargePlan(org.csapi.dsc.TpDataSessionChargeOrder ChargeOrderType, java.lang.String Currency, java.lang.String AdditionalInfo)
	{
		this.ChargeOrderType = ChargeOrderType;
		this.Currency = Currency;
		this.AdditionalInfo = AdditionalInfo;
	}
}
