package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallNetworkAccessType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNetworkAccessType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN = 0;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN);
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_POT = 1;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_POT = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_POT);
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_ISDN = 2;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_ISDN = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_ISDN);
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET = 3;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET);
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_XDSL = 4;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_XDSL = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_XDSL);
	public static final int _P_CALL_NETWORK_ACCESS_TYPE_WIRELESS = 5;
	public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_WIRELESS = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_WIRELESS);
	public int value()
	{
		return value;
	}
	public static TpCallNetworkAccessType from_int(int value)
	{
		switch (value) {
			case _P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN: return P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN;
			case _P_CALL_NETWORK_ACCESS_TYPE_POT: return P_CALL_NETWORK_ACCESS_TYPE_POT;
			case _P_CALL_NETWORK_ACCESS_TYPE_ISDN: return P_CALL_NETWORK_ACCESS_TYPE_ISDN;
			case _P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET: return P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET;
			case _P_CALL_NETWORK_ACCESS_TYPE_XDSL: return P_CALL_NETWORK_ACCESS_TYPE_XDSL;
			case _P_CALL_NETWORK_ACCESS_TYPE_WIRELESS: return P_CALL_NETWORK_ACCESS_TYPE_WIRELESS;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallNetworkAccessType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
