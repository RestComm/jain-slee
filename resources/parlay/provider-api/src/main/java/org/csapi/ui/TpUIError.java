package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIError"
 *	@author JacORB IDL compiler 
 */

public final class TpUIError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_ERROR_UNDEFINED = 0;
	public static final TpUIError P_UI_ERROR_UNDEFINED = new TpUIError(_P_UI_ERROR_UNDEFINED);
	public static final int _P_UI_ERROR_ILLEGAL_INFO = 1;
	public static final TpUIError P_UI_ERROR_ILLEGAL_INFO = new TpUIError(_P_UI_ERROR_ILLEGAL_INFO);
	public static final int _P_UI_ERROR_ID_NOT_FOUND = 2;
	public static final TpUIError P_UI_ERROR_ID_NOT_FOUND = new TpUIError(_P_UI_ERROR_ID_NOT_FOUND);
	public static final int _P_UI_ERROR_RESOURCE_UNAVAILABLE = 3;
	public static final TpUIError P_UI_ERROR_RESOURCE_UNAVAILABLE = new TpUIError(_P_UI_ERROR_RESOURCE_UNAVAILABLE);
	public static final int _P_UI_ERROR_ILLEGAL_RANGE = 4;
	public static final TpUIError P_UI_ERROR_ILLEGAL_RANGE = new TpUIError(_P_UI_ERROR_ILLEGAL_RANGE);
	public static final int _P_UI_ERROR_IMPROPER_USER_RESPONSE = 5;
	public static final TpUIError P_UI_ERROR_IMPROPER_USER_RESPONSE = new TpUIError(_P_UI_ERROR_IMPROPER_USER_RESPONSE);
	public static final int _P_UI_ERROR_ABANDON = 6;
	public static final TpUIError P_UI_ERROR_ABANDON = new TpUIError(_P_UI_ERROR_ABANDON);
	public static final int _P_UI_ERROR_NO_OPERATION_ACTIVE = 7;
	public static final TpUIError P_UI_ERROR_NO_OPERATION_ACTIVE = new TpUIError(_P_UI_ERROR_NO_OPERATION_ACTIVE);
	public static final int _P_UI_ERROR_NO_SPACE_AVAILABLE = 8;
	public static final TpUIError P_UI_ERROR_NO_SPACE_AVAILABLE = new TpUIError(_P_UI_ERROR_NO_SPACE_AVAILABLE);
	public static final int _P_UI_ERROR_RESOURCE_TIMEOUT = 9;
	public static final TpUIError P_UI_ERROR_RESOURCE_TIMEOUT = new TpUIError(_P_UI_ERROR_RESOURCE_TIMEOUT);
	public int value()
	{
		return value;
	}
	public static TpUIError from_int(int value)
	{
		switch (value) {
			case _P_UI_ERROR_UNDEFINED: return P_UI_ERROR_UNDEFINED;
			case _P_UI_ERROR_ILLEGAL_INFO: return P_UI_ERROR_ILLEGAL_INFO;
			case _P_UI_ERROR_ID_NOT_FOUND: return P_UI_ERROR_ID_NOT_FOUND;
			case _P_UI_ERROR_RESOURCE_UNAVAILABLE: return P_UI_ERROR_RESOURCE_UNAVAILABLE;
			case _P_UI_ERROR_ILLEGAL_RANGE: return P_UI_ERROR_ILLEGAL_RANGE;
			case _P_UI_ERROR_IMPROPER_USER_RESPONSE: return P_UI_ERROR_IMPROPER_USER_RESPONSE;
			case _P_UI_ERROR_ABANDON: return P_UI_ERROR_ABANDON;
			case _P_UI_ERROR_NO_OPERATION_ACTIVE: return P_UI_ERROR_NO_OPERATION_ACTIVE;
			case _P_UI_ERROR_NO_SPACE_AVAILABLE: return P_UI_ERROR_NO_SPACE_AVAILABLE;
			case _P_UI_ERROR_RESOURCE_TIMEOUT: return P_UI_ERROR_RESOURCE_TIMEOUT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
