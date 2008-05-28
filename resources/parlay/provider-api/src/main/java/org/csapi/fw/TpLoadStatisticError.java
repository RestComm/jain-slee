package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticError"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_LOAD_INFO_ERROR_UNDEFINED = 0;
	public static final TpLoadStatisticError P_LOAD_INFO_ERROR_UNDEFINED = new TpLoadStatisticError(_P_LOAD_INFO_ERROR_UNDEFINED);
	public static final int _P_LOAD_INFO_UNAVAILABLE = 1;
	public static final TpLoadStatisticError P_LOAD_INFO_UNAVAILABLE = new TpLoadStatisticError(_P_LOAD_INFO_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpLoadStatisticError from_int(int value)
	{
		switch (value) {
			case _P_LOAD_INFO_ERROR_UNDEFINED: return P_LOAD_INFO_ERROR_UNDEFINED;
			case _P_LOAD_INFO_UNAVAILABLE: return P_LOAD_INFO_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLoadStatisticError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
