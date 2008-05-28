package org.csapi;

/**
 *	Generated from IDL definition of union "TpAoCOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpAoCOrder
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.TpCallAoCOrderCategory discriminator;
	private org.csapi.TpChargeAdviceInfo ChargeAdviceInfo;
	private org.csapi.TpChargePerTime ChargePerTime;
	private java.lang.String NetworkCharge;

	public TpAoCOrder ()
	{
	}

	public org.csapi.TpCallAoCOrderCategory discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpChargeAdviceInfo ChargeAdviceInfo ()
	{
		if (discriminator != org.csapi.TpCallAoCOrderCategory.P_CHARGE_ADVICE_INFO)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ChargeAdviceInfo;
	}

	public void ChargeAdviceInfo (org.csapi.TpChargeAdviceInfo _x)
	{
		discriminator = org.csapi.TpCallAoCOrderCategory.P_CHARGE_ADVICE_INFO;
		ChargeAdviceInfo = _x;
	}

	public org.csapi.TpChargePerTime ChargePerTime ()
	{
		if (discriminator != org.csapi.TpCallAoCOrderCategory.P_CHARGE_PER_TIME)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ChargePerTime;
	}

	public void ChargePerTime (org.csapi.TpChargePerTime _x)
	{
		discriminator = org.csapi.TpCallAoCOrderCategory.P_CHARGE_PER_TIME;
		ChargePerTime = _x;
	}

	public java.lang.String NetworkCharge ()
	{
		if (discriminator != org.csapi.TpCallAoCOrderCategory.P_CHARGE_NETWORK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return NetworkCharge;
	}

	public void NetworkCharge (java.lang.String _x)
	{
		discriminator = org.csapi.TpCallAoCOrderCategory.P_CHARGE_NETWORK;
		NetworkCharge = _x;
	}

}
