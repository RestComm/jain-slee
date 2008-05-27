package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallErrorType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallErrorType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_ERROR_UNDEFINED = 0;
	public static final TpCallErrorType P_CALL_ERROR_UNDEFINED = new TpCallErrorType(_P_CALL_ERROR_UNDEFINED);
	public static final int _P_CALL_ERROR_INVALID_ADDRESS = 1;
	public static final TpCallErrorType P_CALL_ERROR_INVALID_ADDRESS = new TpCallErrorType(_P_CALL_ERROR_INVALID_ADDRESS);
	public static final int _P_CALL_ERROR_INVALID_STATE = 2;
	public static final TpCallErrorType P_CALL_ERROR_INVALID_STATE = new TpCallErrorType(_P_CALL_ERROR_INVALID_STATE);
	public static final int _P_CALL_ERROR_RESOURCE_UNAVAILABLE = 3;
	public static final TpCallErrorType P_CALL_ERROR_RESOURCE_UNAVAILABLE = new TpCallErrorType(_P_CALL_ERROR_RESOURCE_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpCallErrorType from_int(int value)
	{
		switch (value) {
			case _P_CALL_ERROR_UNDEFINED: return P_CALL_ERROR_UNDEFINED;
			case _P_CALL_ERROR_INVALID_ADDRESS: return P_CALL_ERROR_INVALID_ADDRESS;
			case _P_CALL_ERROR_INVALID_STATE: return P_CALL_ERROR_INVALID_STATE;
			case _P_CALL_ERROR_RESOURCE_UNAVAILABLE: return P_CALL_ERROR_RESOURCE_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallErrorType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
