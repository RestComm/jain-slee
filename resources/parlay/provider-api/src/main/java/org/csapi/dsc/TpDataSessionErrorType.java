package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionErrorType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_DATA_SESSION_ERROR_UNDEFINED = 0;
	public static final TpDataSessionErrorType P_DATA_SESSION_ERROR_UNDEFINED = new TpDataSessionErrorType(_P_DATA_SESSION_ERROR_UNDEFINED);
	public static final int _P_DATA_SESSION_ERROR_INVALID_ADDRESS = 1;
	public static final TpDataSessionErrorType P_DATA_SESSION_ERROR_INVALID_ADDRESS = new TpDataSessionErrorType(_P_DATA_SESSION_ERROR_INVALID_ADDRESS);
	public static final int _P_DATA_SESSION_ERROR_INVALID_STATE = 2;
	public static final TpDataSessionErrorType P_DATA_SESSION_ERROR_INVALID_STATE = new TpDataSessionErrorType(_P_DATA_SESSION_ERROR_INVALID_STATE);
	public int value()
	{
		return value;
	}
	public static TpDataSessionErrorType from_int(int value)
	{
		switch (value) {
			case _P_DATA_SESSION_ERROR_UNDEFINED: return P_DATA_SESSION_ERROR_UNDEFINED;
			case _P_DATA_SESSION_ERROR_INVALID_ADDRESS: return P_DATA_SESSION_ERROR_INVALID_ADDRESS;
			case _P_DATA_SESSION_ERROR_INVALID_STATE: return P_DATA_SESSION_ERROR_INVALID_STATE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDataSessionErrorType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
