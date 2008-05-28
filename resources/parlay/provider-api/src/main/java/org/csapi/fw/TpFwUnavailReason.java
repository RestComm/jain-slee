package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpFwUnavailReason
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _FW_UNAVAILABLE_UNDEFINED = 0;
	public static final TpFwUnavailReason FW_UNAVAILABLE_UNDEFINED = new TpFwUnavailReason(_FW_UNAVAILABLE_UNDEFINED);
	public static final int _FW_UNAVAILABLE_LOCAL_FAILURE = 1;
	public static final TpFwUnavailReason FW_UNAVAILABLE_LOCAL_FAILURE = new TpFwUnavailReason(_FW_UNAVAILABLE_LOCAL_FAILURE);
	public static final int _FW_UNAVAILABLE_GATEWAY_FAILURE = 2;
	public static final TpFwUnavailReason FW_UNAVAILABLE_GATEWAY_FAILURE = new TpFwUnavailReason(_FW_UNAVAILABLE_GATEWAY_FAILURE);
	public static final int _FW_UNAVAILABLE_OVERLOADED = 3;
	public static final TpFwUnavailReason FW_UNAVAILABLE_OVERLOADED = new TpFwUnavailReason(_FW_UNAVAILABLE_OVERLOADED);
	public static final int _FW_UNAVAILABLE_CLOSED = 4;
	public static final TpFwUnavailReason FW_UNAVAILABLE_CLOSED = new TpFwUnavailReason(_FW_UNAVAILABLE_CLOSED);
	public static final int _FW_UNAVAILABLE_PROTOCOL_FAILURE = 5;
	public static final TpFwUnavailReason FW_UNAVAILABLE_PROTOCOL_FAILURE = new TpFwUnavailReason(_FW_UNAVAILABLE_PROTOCOL_FAILURE);
	public int value()
	{
		return value;
	}
	public static TpFwUnavailReason from_int(int value)
	{
		switch (value) {
			case _FW_UNAVAILABLE_UNDEFINED: return FW_UNAVAILABLE_UNDEFINED;
			case _FW_UNAVAILABLE_LOCAL_FAILURE: return FW_UNAVAILABLE_LOCAL_FAILURE;
			case _FW_UNAVAILABLE_GATEWAY_FAILURE: return FW_UNAVAILABLE_GATEWAY_FAILURE;
			case _FW_UNAVAILABLE_OVERLOADED: return FW_UNAVAILABLE_OVERLOADED;
			case _FW_UNAVAILABLE_CLOSED: return FW_UNAVAILABLE_CLOSED;
			case _FW_UNAVAILABLE_PROTOCOL_FAILURE: return FW_UNAVAILABLE_PROTOCOL_FAILURE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpFwUnavailReason(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
