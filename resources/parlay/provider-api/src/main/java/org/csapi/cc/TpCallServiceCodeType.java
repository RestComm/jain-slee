package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallServiceCodeType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_SERVICE_CODE_UNDEFINED = 0;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_UNDEFINED = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_UNDEFINED);
	public static final int _P_CALL_SERVICE_CODE_DIGITS = 1;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_DIGITS = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_DIGITS);
	public static final int _P_CALL_SERVICE_CODE_FACILITY = 2;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_FACILITY = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_FACILITY);
	public static final int _P_CALL_SERVICE_CODE_U2U = 3;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_U2U = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_U2U);
	public static final int _P_CALL_SERVICE_CODE_HOOKFLASH = 4;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_HOOKFLASH = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_HOOKFLASH);
	public static final int _P_CALL_SERVICE_CODE_RECALL = 5;
	public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_RECALL = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_RECALL);
	public int value()
	{
		return value;
	}
	public static TpCallServiceCodeType from_int(int value)
	{
		switch (value) {
			case _P_CALL_SERVICE_CODE_UNDEFINED: return P_CALL_SERVICE_CODE_UNDEFINED;
			case _P_CALL_SERVICE_CODE_DIGITS: return P_CALL_SERVICE_CODE_DIGITS;
			case _P_CALL_SERVICE_CODE_FACILITY: return P_CALL_SERVICE_CODE_FACILITY;
			case _P_CALL_SERVICE_CODE_U2U: return P_CALL_SERVICE_CODE_U2U;
			case _P_CALL_SERVICE_CODE_HOOKFLASH: return P_CALL_SERVICE_CODE_HOOKFLASH;
			case _P_CALL_SERVICE_CODE_RECALL: return P_CALL_SERVICE_CODE_RECALL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallServiceCodeType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
