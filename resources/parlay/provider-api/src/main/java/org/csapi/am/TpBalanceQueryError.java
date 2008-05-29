package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpBalanceQueryError"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceQueryError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_BALANCE_QUERY_OK = 0;
	public static final TpBalanceQueryError P_BALANCE_QUERY_OK = new TpBalanceQueryError(_P_BALANCE_QUERY_OK);
	public static final int _P_BALANCE_QUERY_ERROR_UNDEFINED = 1;
	public static final TpBalanceQueryError P_BALANCE_QUERY_ERROR_UNDEFINED = new TpBalanceQueryError(_P_BALANCE_QUERY_ERROR_UNDEFINED);
	public static final int _P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER = 2;
	public static final TpBalanceQueryError P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER = new TpBalanceQueryError(_P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER);
	public static final int _P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION = 3;
	public static final TpBalanceQueryError P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION = new TpBalanceQueryError(_P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION);
	public static final int _P_BALANCE_QUERY_SYSTEM_FAILURE = 4;
	public static final TpBalanceQueryError P_BALANCE_QUERY_SYSTEM_FAILURE = new TpBalanceQueryError(_P_BALANCE_QUERY_SYSTEM_FAILURE);
	public int value()
	{
		return value;
	}
	public static TpBalanceQueryError from_int(int value)
	{
		switch (value) {
			case _P_BALANCE_QUERY_OK: return P_BALANCE_QUERY_OK;
			case _P_BALANCE_QUERY_ERROR_UNDEFINED: return P_BALANCE_QUERY_ERROR_UNDEFINED;
			case _P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER: return P_BALANCE_QUERY_UNKNOWN_SUBSCRIBER;
			case _P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION: return P_BALANCE_QUERY_UNAUTHORIZED_APPLICATION;
			case _P_BALANCE_QUERY_SYSTEM_FAILURE: return P_BALANCE_QUERY_SYSTEM_FAILURE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpBalanceQueryError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
