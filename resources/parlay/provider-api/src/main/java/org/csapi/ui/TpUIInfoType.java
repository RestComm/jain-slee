package org.csapi.ui;
/**
 *	Generated from IDL definition of enum "TpUIInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfoType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UI_INFO_ID = 0;
	public static final TpUIInfoType P_UI_INFO_ID = new TpUIInfoType(_P_UI_INFO_ID);
	public static final int _P_UI_INFO_DATA = 1;
	public static final TpUIInfoType P_UI_INFO_DATA = new TpUIInfoType(_P_UI_INFO_DATA);
	public static final int _P_UI_INFO_ADDRESS = 2;
	public static final TpUIInfoType P_UI_INFO_ADDRESS = new TpUIInfoType(_P_UI_INFO_ADDRESS);
	public static final int _P_UI_INFO_BIN_DATA = 3;
	public static final TpUIInfoType P_UI_INFO_BIN_DATA = new TpUIInfoType(_P_UI_INFO_BIN_DATA);
	public static final int _P_UI_INFO_UUENCODED = 4;
	public static final TpUIInfoType P_UI_INFO_UUENCODED = new TpUIInfoType(_P_UI_INFO_UUENCODED);
	public static final int _P_UI_INFO_MIME = 5;
	public static final TpUIInfoType P_UI_INFO_MIME = new TpUIInfoType(_P_UI_INFO_MIME);
	public static final int _P_UI_INFO_WAVE = 6;
	public static final TpUIInfoType P_UI_INFO_WAVE = new TpUIInfoType(_P_UI_INFO_WAVE);
	public static final int _P_UI_INFO_AU = 7;
	public static final TpUIInfoType P_UI_INFO_AU = new TpUIInfoType(_P_UI_INFO_AU);
	public int value()
	{
		return value;
	}
	public static TpUIInfoType from_int(int value)
	{
		switch (value) {
			case _P_UI_INFO_ID: return P_UI_INFO_ID;
			case _P_UI_INFO_DATA: return P_UI_INFO_DATA;
			case _P_UI_INFO_ADDRESS: return P_UI_INFO_ADDRESS;
			case _P_UI_INFO_BIN_DATA: return P_UI_INFO_BIN_DATA;
			case _P_UI_INFO_UUENCODED: return P_UI_INFO_UUENCODED;
			case _P_UI_INFO_MIME: return P_UI_INFO_MIME;
			case _P_UI_INFO_WAVE: return P_UI_INFO_WAVE;
			case _P_UI_INFO_AU: return P_UI_INFO_AU;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUIInfoType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
