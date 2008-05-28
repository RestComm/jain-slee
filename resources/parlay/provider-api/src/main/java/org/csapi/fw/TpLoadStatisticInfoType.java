package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfoType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_LOAD_STATISTICS_VALID = 0;
	public static final TpLoadStatisticInfoType P_LOAD_STATISTICS_VALID = new TpLoadStatisticInfoType(_P_LOAD_STATISTICS_VALID);
	public static final int _P_LOAD_STATISTICS_INVALID = 1;
	public static final TpLoadStatisticInfoType P_LOAD_STATISTICS_INVALID = new TpLoadStatisticInfoType(_P_LOAD_STATISTICS_INVALID);
	public int value()
	{
		return value;
	}
	public static TpLoadStatisticInfoType from_int(int value)
	{
		switch (value) {
			case _P_LOAD_STATISTICS_VALID: return P_LOAD_STATISTICS_VALID;
			case _P_LOAD_STATISTICS_INVALID: return P_LOAD_STATISTICS_INVALID;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLoadStatisticInfoType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
