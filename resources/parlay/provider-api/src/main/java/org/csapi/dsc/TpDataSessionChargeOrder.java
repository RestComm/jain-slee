package org.csapi.dsc;

/**
 *	Generated from IDL definition of union "TpDataSessionChargeOrder"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrder
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.dsc.TpDataSessionChargeOrderCategory discriminator;
	private org.csapi.dsc.TpChargePerVolume ChargePerVolume;
	private java.lang.String NetworkCharge;

	public TpDataSessionChargeOrder ()
	{
	}

	public org.csapi.dsc.TpDataSessionChargeOrderCategory discriminator ()
	{
		return discriminator;
	}

	public org.csapi.dsc.TpChargePerVolume ChargePerVolume ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_PER_VOLUME)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ChargePerVolume;
	}

	public void ChargePerVolume (org.csapi.dsc.TpChargePerVolume _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_PER_VOLUME;
		ChargePerVolume = _x;
	}

	public java.lang.String NetworkCharge ()
	{
		if (discriminator != org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_NETWORK)
			throw new org.omg.CORBA.BAD_OPERATION();
		return NetworkCharge;
	}

	public void NetworkCharge (java.lang.String _x)
	{
		discriminator = org.csapi.dsc.TpDataSessionChargeOrderCategory.P_DATA_SESSION_CHARGE_NETWORK;
		NetworkCharge = _x;
	}

}
