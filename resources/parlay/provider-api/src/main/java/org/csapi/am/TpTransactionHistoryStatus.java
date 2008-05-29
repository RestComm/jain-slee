package org.csapi.am;
/**
 *	Generated from IDL definition of enum "TpTransactionHistoryStatus"
 *	@author JacORB IDL compiler 
 */

public final class TpTransactionHistoryStatus
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_AM_TRANSACTION_ERROR_UNSPECIFIED = 0;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_ERROR_UNSPECIFIED = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_ERROR_UNSPECIFIED);
	public static final int _P_AM_TRANSACTION_INVALID_INTERVAL = 1;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_INVALID_INTERVAL = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_INVALID_INTERVAL);
	public static final int _P_AM_TRANSACTION_UNKNOWN_ACCOUNT = 2;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_UNKNOWN_ACCOUNT = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_UNKNOWN_ACCOUNT);
	public static final int _P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION = 3;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION);
	public static final int _P_AM_TRANSACTION_PROCESSING_ERROR = 4;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_PROCESSING_ERROR = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_PROCESSING_ERROR);
	public static final int _P_AM_TRANSACTION_SYSTEM_FAILURE = 5;
	public static final TpTransactionHistoryStatus P_AM_TRANSACTION_SYSTEM_FAILURE = new TpTransactionHistoryStatus(_P_AM_TRANSACTION_SYSTEM_FAILURE);
	public int value()
	{
		return value;
	}
	public static TpTransactionHistoryStatus from_int(int value)
	{
		switch (value) {
			case _P_AM_TRANSACTION_ERROR_UNSPECIFIED: return P_AM_TRANSACTION_ERROR_UNSPECIFIED;
			case _P_AM_TRANSACTION_INVALID_INTERVAL: return P_AM_TRANSACTION_INVALID_INTERVAL;
			case _P_AM_TRANSACTION_UNKNOWN_ACCOUNT: return P_AM_TRANSACTION_UNKNOWN_ACCOUNT;
			case _P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION: return P_AM_TRANSACTION_UNAUTHORIZED_APPLICATION;
			case _P_AM_TRANSACTION_PROCESSING_ERROR: return P_AM_TRANSACTION_PROCESSING_ERROR;
			case _P_AM_TRANSACTION_SYSTEM_FAILURE: return P_AM_TRANSACTION_SYSTEM_FAILURE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTransactionHistoryStatus(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
