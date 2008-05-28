package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAoCInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAoCInfo(){}
	public org.csapi.TpAoCOrder ChargeOrder;
	public java.lang.String Currency;
	public TpAoCInfo(org.csapi.TpAoCOrder ChargeOrder, java.lang.String Currency)
	{
		this.ChargeOrder = ChargeOrder;
		this.Currency = Currency;
	}
}
