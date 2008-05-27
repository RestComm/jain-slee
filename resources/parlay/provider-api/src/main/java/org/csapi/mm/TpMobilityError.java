package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityError"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityError
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_OK = 0;
	public static final TpMobilityError P_M_OK = new TpMobilityError(_P_M_OK);
	public static final int _P_M_SYSTEM_FAILURE = 1;
	public static final TpMobilityError P_M_SYSTEM_FAILURE = new TpMobilityError(_P_M_SYSTEM_FAILURE);
	public static final int _P_M_UNAUTHORIZED_NETWORK = 2;
	public static final TpMobilityError P_M_UNAUTHORIZED_NETWORK = new TpMobilityError(_P_M_UNAUTHORIZED_NETWORK);
	public static final int _P_M_UNAUTHORIZED_APPLICATION = 3;
	public static final TpMobilityError P_M_UNAUTHORIZED_APPLICATION = new TpMobilityError(_P_M_UNAUTHORIZED_APPLICATION);
	public static final int _P_M_UNKNOWN_SUBSCRIBER = 4;
	public static final TpMobilityError P_M_UNKNOWN_SUBSCRIBER = new TpMobilityError(_P_M_UNKNOWN_SUBSCRIBER);
	public static final int _P_M_ABSENT_SUBSCRIBER = 5;
	public static final TpMobilityError P_M_ABSENT_SUBSCRIBER = new TpMobilityError(_P_M_ABSENT_SUBSCRIBER);
	public static final int _P_M_POSITION_METHOD_FAILURE = 6;
	public static final TpMobilityError P_M_POSITION_METHOD_FAILURE = new TpMobilityError(_P_M_POSITION_METHOD_FAILURE);
	public int value()
	{
		return value;
	}
	public static TpMobilityError from_int(int value)
	{
		switch (value) {
			case _P_M_OK: return P_M_OK;
			case _P_M_SYSTEM_FAILURE: return P_M_SYSTEM_FAILURE;
			case _P_M_UNAUTHORIZED_NETWORK: return P_M_UNAUTHORIZED_NETWORK;
			case _P_M_UNAUTHORIZED_APPLICATION: return P_M_UNAUTHORIZED_APPLICATION;
			case _P_M_UNKNOWN_SUBSCRIBER: return P_M_UNKNOWN_SUBSCRIBER;
			case _P_M_ABSENT_SUBSCRIBER: return P_M_ABSENT_SUBSCRIBER;
			case _P_M_POSITION_METHOD_FAILURE: return P_M_POSITION_METHOD_FAILURE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMobilityError(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
