package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallAppInfoType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAppInfoType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_APP_UNDEFINED = 0;
	public static final TpCallAppInfoType P_CALL_APP_UNDEFINED = new TpCallAppInfoType(_P_CALL_APP_UNDEFINED);
	public static final int _P_CALL_APP_ALERTING_MECHANISM = 1;
	public static final TpCallAppInfoType P_CALL_APP_ALERTING_MECHANISM = new TpCallAppInfoType(_P_CALL_APP_ALERTING_MECHANISM);
	public static final int _P_CALL_APP_NETWORK_ACCESS_TYPE = 2;
	public static final TpCallAppInfoType P_CALL_APP_NETWORK_ACCESS_TYPE = new TpCallAppInfoType(_P_CALL_APP_NETWORK_ACCESS_TYPE);
	public static final int _P_CALL_APP_TELE_SERVICE = 3;
	public static final TpCallAppInfoType P_CALL_APP_TELE_SERVICE = new TpCallAppInfoType(_P_CALL_APP_TELE_SERVICE);
	public static final int _P_CALL_APP_BEARER_SERVICE = 4;
	public static final TpCallAppInfoType P_CALL_APP_BEARER_SERVICE = new TpCallAppInfoType(_P_CALL_APP_BEARER_SERVICE);
	public static final int _P_CALL_APP_PARTY_CATEGORY = 5;
	public static final TpCallAppInfoType P_CALL_APP_PARTY_CATEGORY = new TpCallAppInfoType(_P_CALL_APP_PARTY_CATEGORY);
	public static final int _P_CALL_APP_PRESENTATION_ADDRESS = 6;
	public static final TpCallAppInfoType P_CALL_APP_PRESENTATION_ADDRESS = new TpCallAppInfoType(_P_CALL_APP_PRESENTATION_ADDRESS);
	public static final int _P_CALL_APP_GENERIC_INFO = 7;
	public static final TpCallAppInfoType P_CALL_APP_GENERIC_INFO = new TpCallAppInfoType(_P_CALL_APP_GENERIC_INFO);
	public static final int _P_CALL_APP_ADDITIONAL_ADDRESS = 8;
	public static final TpCallAppInfoType P_CALL_APP_ADDITIONAL_ADDRESS = new TpCallAppInfoType(_P_CALL_APP_ADDITIONAL_ADDRESS);
	public int value()
	{
		return value;
	}
	public static TpCallAppInfoType from_int(int value)
	{
		switch (value) {
			case _P_CALL_APP_UNDEFINED: return P_CALL_APP_UNDEFINED;
			case _P_CALL_APP_ALERTING_MECHANISM: return P_CALL_APP_ALERTING_MECHANISM;
			case _P_CALL_APP_NETWORK_ACCESS_TYPE: return P_CALL_APP_NETWORK_ACCESS_TYPE;
			case _P_CALL_APP_TELE_SERVICE: return P_CALL_APP_TELE_SERVICE;
			case _P_CALL_APP_BEARER_SERVICE: return P_CALL_APP_BEARER_SERVICE;
			case _P_CALL_APP_PARTY_CATEGORY: return P_CALL_APP_PARTY_CATEGORY;
			case _P_CALL_APP_PRESENTATION_ADDRESS: return P_CALL_APP_PRESENTATION_ADDRESS;
			case _P_CALL_APP_GENERIC_INFO: return P_CALL_APP_GENERIC_INFO;
			case _P_CALL_APP_ADDITIONAL_ADDRESS: return P_CALL_APP_ADDITIONAL_ADDRESS;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallAppInfoType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
