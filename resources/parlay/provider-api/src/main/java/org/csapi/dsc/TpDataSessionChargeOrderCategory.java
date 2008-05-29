package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionChargeOrderCategory"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionChargeOrderCategory
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_DATA_SESSION_CHARGE_PER_VOLUME = 0;
	public static final TpDataSessionChargeOrderCategory P_DATA_SESSION_CHARGE_PER_VOLUME = new TpDataSessionChargeOrderCategory(_P_DATA_SESSION_CHARGE_PER_VOLUME);
	public static final int _P_DATA_SESSION_CHARGE_NETWORK = 1;
	public static final TpDataSessionChargeOrderCategory P_DATA_SESSION_CHARGE_NETWORK = new TpDataSessionChargeOrderCategory(_P_DATA_SESSION_CHARGE_NETWORK);
	public int value()
	{
		return value;
	}
	public static TpDataSessionChargeOrderCategory from_int(int value)
	{
		switch (value) {
			case _P_DATA_SESSION_CHARGE_PER_VOLUME: return P_DATA_SESSION_CHARGE_PER_VOLUME;
			case _P_DATA_SESSION_CHARGE_NETWORK: return P_DATA_SESSION_CHARGE_NETWORK;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDataSessionChargeOrderCategory(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
