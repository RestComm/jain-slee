package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallReportType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallReportType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_REPORT_UNDEFINED = 0;
	public static final TpCallReportType P_CALL_REPORT_UNDEFINED = new TpCallReportType(_P_CALL_REPORT_UNDEFINED);
	public static final int _P_CALL_REPORT_PROGRESS = 1;
	public static final TpCallReportType P_CALL_REPORT_PROGRESS = new TpCallReportType(_P_CALL_REPORT_PROGRESS);
	public static final int _P_CALL_REPORT_ALERTING = 2;
	public static final TpCallReportType P_CALL_REPORT_ALERTING = new TpCallReportType(_P_CALL_REPORT_ALERTING);
	public static final int _P_CALL_REPORT_ANSWER = 3;
	public static final TpCallReportType P_CALL_REPORT_ANSWER = new TpCallReportType(_P_CALL_REPORT_ANSWER);
	public static final int _P_CALL_REPORT_BUSY = 4;
	public static final TpCallReportType P_CALL_REPORT_BUSY = new TpCallReportType(_P_CALL_REPORT_BUSY);
	public static final int _P_CALL_REPORT_NO_ANSWER = 5;
	public static final TpCallReportType P_CALL_REPORT_NO_ANSWER = new TpCallReportType(_P_CALL_REPORT_NO_ANSWER);
	public static final int _P_CALL_REPORT_DISCONNECT = 6;
	public static final TpCallReportType P_CALL_REPORT_DISCONNECT = new TpCallReportType(_P_CALL_REPORT_DISCONNECT);
	public static final int _P_CALL_REPORT_REDIRECTED = 7;
	public static final TpCallReportType P_CALL_REPORT_REDIRECTED = new TpCallReportType(_P_CALL_REPORT_REDIRECTED);
	public static final int _P_CALL_REPORT_SERVICE_CODE = 8;
	public static final TpCallReportType P_CALL_REPORT_SERVICE_CODE = new TpCallReportType(_P_CALL_REPORT_SERVICE_CODE);
	public static final int _P_CALL_REPORT_ROUTING_FAILURE = 9;
	public static final TpCallReportType P_CALL_REPORT_ROUTING_FAILURE = new TpCallReportType(_P_CALL_REPORT_ROUTING_FAILURE);
	public static final int _P_CALL_REPORT_QUEUED = 10;
	public static final TpCallReportType P_CALL_REPORT_QUEUED = new TpCallReportType(_P_CALL_REPORT_QUEUED);
	public static final int _P_CALL_REPORT_NOT_REACHABLE = 11;
	public static final TpCallReportType P_CALL_REPORT_NOT_REACHABLE = new TpCallReportType(_P_CALL_REPORT_NOT_REACHABLE);
	public int value()
	{
		return value;
	}
	public static TpCallReportType from_int(int value)
	{
		switch (value) {
			case _P_CALL_REPORT_UNDEFINED: return P_CALL_REPORT_UNDEFINED;
			case _P_CALL_REPORT_PROGRESS: return P_CALL_REPORT_PROGRESS;
			case _P_CALL_REPORT_ALERTING: return P_CALL_REPORT_ALERTING;
			case _P_CALL_REPORT_ANSWER: return P_CALL_REPORT_ANSWER;
			case _P_CALL_REPORT_BUSY: return P_CALL_REPORT_BUSY;
			case _P_CALL_REPORT_NO_ANSWER: return P_CALL_REPORT_NO_ANSWER;
			case _P_CALL_REPORT_DISCONNECT: return P_CALL_REPORT_DISCONNECT;
			case _P_CALL_REPORT_REDIRECTED: return P_CALL_REPORT_REDIRECTED;
			case _P_CALL_REPORT_SERVICE_CODE: return P_CALL_REPORT_SERVICE_CODE;
			case _P_CALL_REPORT_ROUTING_FAILURE: return P_CALL_REPORT_ROUTING_FAILURE;
			case _P_CALL_REPORT_QUEUED: return P_CALL_REPORT_QUEUED;
			case _P_CALL_REPORT_NOT_REACHABLE: return P_CALL_REPORT_NOT_REACHABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallReportType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
