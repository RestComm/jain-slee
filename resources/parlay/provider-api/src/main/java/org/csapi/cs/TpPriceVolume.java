package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpPriceVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpPriceVolume
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPriceVolume(){}
	public org.csapi.cs.TpChargingPrice Price;
	public org.csapi.cs.TpVolume Volume;
	public TpPriceVolume(org.csapi.cs.TpChargingPrice Price, org.csapi.cs.TpVolume Volume)
	{
		this.Price = Price;
		this.Volume = Volume;
	}
}
