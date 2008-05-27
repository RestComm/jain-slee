package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpChargePerVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerVolume
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargePerVolume(){}
	public int InitialCharge;
	public int CurrentChargePerKilobyte;
	public int NextChargePerKilobyte;
	public TpChargePerVolume(int InitialCharge, int CurrentChargePerKilobyte, int NextChargePerKilobyte)
	{
		this.InitialCharge = InitialCharge;
		this.CurrentChargePerKilobyte = CurrentChargePerKilobyte;
		this.NextChargePerKilobyte = NextChargePerKilobyte;
	}
}
