package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpChargingEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_AM_CHARGING = 0;
	public static final TpChargingEventName P_AM_CHARGING = new TpChargingEventName(_P_AM_CHARGING);
	public static final int _P_AM_RECHARGING = 1;
	public static final TpChargingEventName P_AM_RECHARGING = new TpChargingEventName(_P_AM_RECHARGING);
	public static final int _P_AM_ACCOUNT_LOW = 2;
	public static final TpChargingEventName P_AM_ACCOUNT_LOW = new TpChargingEventName(_P_AM_ACCOUNT_LOW);
	public static final int _P_AM_ACCOUNT_ZERO = 3;
	public static final TpChargingEventName P_AM_ACCOUNT_ZERO = new TpChargingEventName(_P_AM_ACCOUNT_ZERO);
	public static final int _P_AM_ACCOUNT_DISABLED = 4;
	public static final TpChargingEventName P_AM_ACCOUNT_DISABLED = new TpChargingEventName(_P_AM_ACCOUNT_DISABLED);
	public int value()
	{
		return value;
	}
	public static TpChargingEventName from_int(int value)
	{
		switch (value) {
			case _P_AM_CHARGING: return P_AM_CHARGING;
			case _P_AM_RECHARGING: return P_AM_RECHARGING;
			case _P_AM_ACCOUNT_LOW: return P_AM_ACCOUNT_LOW;
			case _P_AM_ACCOUNT_ZERO: return P_AM_ACCOUNT_ZERO;
			case _P_AM_ACCOUNT_DISABLED: return P_AM_ACCOUNT_DISABLED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpChargingEventName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
