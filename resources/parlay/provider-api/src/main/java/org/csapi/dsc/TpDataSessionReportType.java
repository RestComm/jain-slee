package org.csapi.dsc;
/**
 *	Generated from IDL definition of enum "TpDataSessionReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionReportType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_DATA_SESSION_REPORT_UNDEFINED = 0;
	public static final TpDataSessionReportType P_DATA_SESSION_REPORT_UNDEFINED = new TpDataSessionReportType(_P_DATA_SESSION_REPORT_UNDEFINED);
	public static final int _P_DATA_SESSION_REPORT_CONNECTED = 1;
	public static final TpDataSessionReportType P_DATA_SESSION_REPORT_CONNECTED = new TpDataSessionReportType(_P_DATA_SESSION_REPORT_CONNECTED);
	public static final int _P_DATA_SESSION_REPORT_DISCONNECT = 2;
	public static final TpDataSessionReportType P_DATA_SESSION_REPORT_DISCONNECT = new TpDataSessionReportType(_P_DATA_SESSION_REPORT_DISCONNECT);
	public int value()
	{
		return value;
	}
	public static TpDataSessionReportType from_int(int value)
	{
		switch (value) {
			case _P_DATA_SESSION_REPORT_UNDEFINED: return P_DATA_SESSION_REPORT_UNDEFINED;
			case _P_DATA_SESSION_REPORT_CONNECTED: return P_DATA_SESSION_REPORT_CONNECTED;
			case _P_DATA_SESSION_REPORT_DISCONNECT: return P_DATA_SESSION_REPORT_DISCONNECT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpDataSessionReportType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
