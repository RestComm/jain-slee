package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpChargingPrice"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingPrice
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingPrice(){}
	public java.lang.String Currency;
	public org.csapi.cs.TpAmount Amount;
	public TpChargingPrice(java.lang.String Currency, org.csapi.cs.TpAmount Amount)
	{
		this.Currency = Currency;
		this.Amount = Amount;
	}
}
