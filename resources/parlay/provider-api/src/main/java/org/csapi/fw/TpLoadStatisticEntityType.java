package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpLoadStatisticEntityType"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_LOAD_STATISTICS_FW_TYPE = 0;
	public static final TpLoadStatisticEntityType P_LOAD_STATISTICS_FW_TYPE = new TpLoadStatisticEntityType(_P_LOAD_STATISTICS_FW_TYPE);
	public static final int _P_LOAD_STATISTICS_SVC_TYPE = 1;
	public static final TpLoadStatisticEntityType P_LOAD_STATISTICS_SVC_TYPE = new TpLoadStatisticEntityType(_P_LOAD_STATISTICS_SVC_TYPE);
	public static final int _P_LOAD_STATISTICS_APP_TYPE = 2;
	public static final TpLoadStatisticEntityType P_LOAD_STATISTICS_APP_TYPE = new TpLoadStatisticEntityType(_P_LOAD_STATISTICS_APP_TYPE);
	public int value()
	{
		return value;
	}
	public static TpLoadStatisticEntityType from_int(int value)
	{
		switch (value) {
			case _P_LOAD_STATISTICS_FW_TYPE: return P_LOAD_STATISTICS_FW_TYPE;
			case _P_LOAD_STATISTICS_SVC_TYPE: return P_LOAD_STATISTICS_SVC_TYPE;
			case _P_LOAD_STATISTICS_APP_TYPE: return P_LOAD_STATISTICS_APP_TYPE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLoadStatisticEntityType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
