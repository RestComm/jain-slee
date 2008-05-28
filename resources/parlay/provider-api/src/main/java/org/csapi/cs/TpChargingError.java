package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpChargingError"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CHS_ERR_UNDEFINED = 0;
	public static final TpChargingError P_CHS_ERR_UNDEFINED = new TpChargingError(_P_CHS_ERR_UNDEFINED);
	public static final int _P_CHS_ERR_ACCOUNT = 1;
	public static final TpChargingError P_CHS_ERR_ACCOUNT = new TpChargingError(_P_CHS_ERR_ACCOUNT);
	public static final int _P_CHS_ERR_USER = 2;
	public static final TpChargingError P_CHS_ERR_USER = new TpChargingError(_P_CHS_ERR_USER);
	public static final int _P_CHS_ERR_PARAMETER = 3;
	public static final TpChargingError P_CHS_ERR_PARAMETER = new TpChargingError(_P_CHS_ERR_PARAMETER);
	public static final int _P_CHS_ERR_NO_DEBIT = 4;
	public static final TpChargingError P_CHS_ERR_NO_DEBIT = new TpChargingError(_P_CHS_ERR_NO_DEBIT);
	public static final int _P_CHS_ERR_NO_CREDIT = 5;
	public static final TpChargingError P_CHS_ERR_NO_CREDIT = new TpChargingError(_P_CHS_ERR_NO_CREDIT);
	public static final int _P_CHS_ERR_VOLUMES = 6;
	public static final TpChargingError P_CHS_ERR_VOLUMES = new TpChargingError(_P_CHS_ERR_VOLUMES);
	public static final int _P_CHS_ERR_CURRENCY = 7;
	public static final TpChargingError P_CHS_ERR_CURRENCY = new TpChargingError(_P_CHS_ERR_CURRENCY);
	public static final int _P_CHS_ERR_NO_EXTEND = 8;
	public static final TpChargingError P_CHS_ERR_NO_EXTEND = new TpChargingError(_P_CHS_ERR_NO_EXTEND);
	public static final int _P_CHS_ERR_RESERVATION_LIMIT = 9;
	public static final TpChargingError P_CHS_ERR_RESERVATION_LIMIT = new TpChargingError(_P_CHS_ERR_RESERVATION_LIMIT);
	public static final int _P_CHS_ERR_CONFIRMATION_REQUIRED = 10;
	public static final TpChargingError P_CHS_ERR_CONFIRMATION_REQUIRED = new TpChargingError(_P_CHS_ERR_CONFIRMATION_REQUIRED);
	public int value()
	{
		return value;
	}
	public static TpChargingError from_int(int value)
	{
		switch (value) {
			case _P_CHS_ERR_UNDEFINED: return P_CHS_ERR_UNDEFINED;
			case _P_CHS_ERR_ACCOUNT: return P_CHS_ERR_ACCOUNT;
			case _P_CHS_ERR_USER: return P_CHS_ERR_USER;
			case _P_CHS_ERR_PARAMETER: return P_CHS_ERR_PARAMETER;
			case _P_CHS_ERR_NO_DEBIT: return P_CHS_ERR_NO_DEBIT;
			case _P_CHS_ERR_NO_CREDIT: return P_CHS_ERR_NO_CREDIT;
			case _P_CHS_ERR_VOLUMES: return P_CHS_ERR_VOLUMES;
			case _P_CHS_ERR_CURRENCY: return P_CHS_ERR_CURRENCY;
			case _P_CHS_ERR_NO_EXTEND: return P_CHS_ERR_NO_EXTEND;
			case _P_CHS_ERR_RESERVATION_LIMIT: return P_CHS_ERR_RESERVATION_LIMIT;
			case _P_CHS_ERR_CONFIRMATION_REQUIRED: return P_CHS_ERR_CONFIRMATION_REQUIRED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpChargingError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
