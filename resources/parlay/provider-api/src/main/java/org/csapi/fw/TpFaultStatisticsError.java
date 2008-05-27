package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFaultStatisticsError"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStatisticsError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_FAULT_INFO_ERROR_UNDEFINED = 0;
	public static final TpFaultStatisticsError P_FAULT_INFO_ERROR_UNDEFINED = new TpFaultStatisticsError(_P_FAULT_INFO_ERROR_UNDEFINED);
	public static final int _P_FAULT_INFO_UNAVAILABLE = 1;
	public static final TpFaultStatisticsError P_FAULT_INFO_UNAVAILABLE = new TpFaultStatisticsError(_P_FAULT_INFO_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpFaultStatisticsError from_int(int value)
	{
		switch (value) {
			case _P_FAULT_INFO_ERROR_UNDEFINED: return P_FAULT_INFO_ERROR_UNDEFINED;
			case _P_FAULT_INFO_UNAVAILABLE: return P_FAULT_INFO_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpFaultStatisticsError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
