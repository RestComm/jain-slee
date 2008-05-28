package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIEventInfoDataType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventInfoDataType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_EVENT_DATA_TYPE_UNDEFINED = 0;
	public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_UNDEFINED = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_UNDEFINED);
	public static final int _P_UI_EVENT_DATA_TYPE_UNSPECIFIED = 1;
	public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_UNSPECIFIED = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_UNSPECIFIED);
	public static final int _P_UI_EVENT_DATA_TYPE_TEXT = 2;
	public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_TEXT = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_TEXT);
	public static final int _P_UI_EVENT_DATA_TYPE_USSD_DATA = 3;
	public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_USSD_DATA = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_USSD_DATA);
	public int value()
	{
		return value;
	}
	public static TpUIEventInfoDataType from_int(int value)
	{
		switch (value) {
			case _P_UI_EVENT_DATA_TYPE_UNDEFINED: return P_UI_EVENT_DATA_TYPE_UNDEFINED;
			case _P_UI_EVENT_DATA_TYPE_UNSPECIFIED: return P_UI_EVENT_DATA_TYPE_UNSPECIFIED;
			case _P_UI_EVENT_DATA_TYPE_TEXT: return P_UI_EVENT_DATA_TYPE_TEXT;
			case _P_UI_EVENT_DATA_TYPE_USSD_DATA: return P_UI_EVENT_DATA_TYPE_USSD_DATA;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIEventInfoDataType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
