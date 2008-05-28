package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIReport"
 *	@author JacORB IDL compiler 
 */

public final class TpUIReport
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_REPORT_UNDEFINED = 0;
	public static final TpUIReport P_UI_REPORT_UNDEFINED = new TpUIReport(_P_UI_REPORT_UNDEFINED);
	public static final int _P_UI_REPORT_INFO_SENT = 1;
	public static final TpUIReport P_UI_REPORT_INFO_SENT = new TpUIReport(_P_UI_REPORT_INFO_SENT);
	public static final int _P_UI_REPORT_INFO_COLLECTED = 2;
	public static final TpUIReport P_UI_REPORT_INFO_COLLECTED = new TpUIReport(_P_UI_REPORT_INFO_COLLECTED);
	public static final int _P_UI_REPORT_NO_INPUT = 3;
	public static final TpUIReport P_UI_REPORT_NO_INPUT = new TpUIReport(_P_UI_REPORT_NO_INPUT);
	public static final int _P_UI_REPORT_TIMEOUT = 4;
	public static final TpUIReport P_UI_REPORT_TIMEOUT = new TpUIReport(_P_UI_REPORT_TIMEOUT);
	public static final int _P_UI_REPORT_MESSAGE_STORED = 5;
	public static final TpUIReport P_UI_REPORT_MESSAGE_STORED = new TpUIReport(_P_UI_REPORT_MESSAGE_STORED);
	public static final int _P_UI_REPORT_MESSAGE_NOT_STORED = 6;
	public static final TpUIReport P_UI_REPORT_MESSAGE_NOT_STORED = new TpUIReport(_P_UI_REPORT_MESSAGE_NOT_STORED);
	public static final int _P_UI_REPORT_MESSAGE_DELETED = 7;
	public static final TpUIReport P_UI_REPORT_MESSAGE_DELETED = new TpUIReport(_P_UI_REPORT_MESSAGE_DELETED);
	public static final int _P_UI_REPORT_MESSAGE_NOT_DELETED = 8;
	public static final TpUIReport P_UI_REPORT_MESSAGE_NOT_DELETED = new TpUIReport(_P_UI_REPORT_MESSAGE_NOT_DELETED);
	public int value()
	{
		return value;
	}
	public static TpUIReport from_int(int value)
	{
		switch (value) {
			case _P_UI_REPORT_UNDEFINED: return P_UI_REPORT_UNDEFINED;
			case _P_UI_REPORT_INFO_SENT: return P_UI_REPORT_INFO_SENT;
			case _P_UI_REPORT_INFO_COLLECTED: return P_UI_REPORT_INFO_COLLECTED;
			case _P_UI_REPORT_NO_INPUT: return P_UI_REPORT_NO_INPUT;
			case _P_UI_REPORT_TIMEOUT: return P_UI_REPORT_TIMEOUT;
			case _P_UI_REPORT_MESSAGE_STORED: return P_UI_REPORT_MESSAGE_STORED;
			case _P_UI_REPORT_MESSAGE_NOT_STORED: return P_UI_REPORT_MESSAGE_NOT_STORED;
			case _P_UI_REPORT_MESSAGE_DELETED: return P_UI_REPORT_MESSAGE_DELETED;
			case _P_UI_REPORT_MESSAGE_NOT_DELETED: return P_UI_REPORT_MESSAGE_NOT_DELETED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIReport(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
