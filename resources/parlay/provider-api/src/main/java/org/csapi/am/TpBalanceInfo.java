package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpBalanceInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpBalanceInfo(){}
	public java.lang.String Currency;
	public int ValuePartA;
	public int ValuePartB;
	public int Exponent;
	public java.lang.String AdditionalInfo;
	public TpBalanceInfo(java.lang.String Currency, int ValuePartA, int ValuePartB, int Exponent, java.lang.String AdditionalInfo)
	{
		this.Currency = Currency;
		this.ValuePartA = ValuePartA;
		this.ValuePartB = ValuePartB;
		this.Exponent = Exponent;
		this.AdditionalInfo = AdditionalInfo;
	}
}
