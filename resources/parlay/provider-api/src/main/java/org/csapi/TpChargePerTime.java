package org.csapi;

/**
 *	Generated from IDL definition of struct "TpChargePerTime"
 *	@author JacORB IDL compiler 
 */

public final class TpChargePerTime
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargePerTime(){}
	public int InitialCharge;
	public int CurrentChargePerMinute;
	public int NextChargePerMinute;
	public TpChargePerTime(int InitialCharge, int CurrentChargePerMinute, int NextChargePerMinute)
	{
		this.InitialCharge = InitialCharge;
		this.CurrentChargePerMinute = CurrentChargePerMinute;
		this.NextChargePerMinute = NextChargePerMinute;
	}
}
