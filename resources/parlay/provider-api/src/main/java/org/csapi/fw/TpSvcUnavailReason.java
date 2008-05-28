package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcUnavailReason
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SERVICE_UNAVAILABLE_UNDEFINED = 0;
	public static final TpSvcUnavailReason SERVICE_UNAVAILABLE_UNDEFINED = new TpSvcUnavailReason(_SERVICE_UNAVAILABLE_UNDEFINED);
	public static final int _SERVICE_UNAVAILABLE_LOCAL_FAILURE = 1;
	public static final TpSvcUnavailReason SERVICE_UNAVAILABLE_LOCAL_FAILURE = new TpSvcUnavailReason(_SERVICE_UNAVAILABLE_LOCAL_FAILURE);
	public static final int _SERVICE_UNAVAILABLE_GATEWAY_FAILURE = 2;
	public static final TpSvcUnavailReason SERVICE_UNAVAILABLE_GATEWAY_FAILURE = new TpSvcUnavailReason(_SERVICE_UNAVAILABLE_GATEWAY_FAILURE);
	public static final int _SERVICE_UNAVAILABLE_OVERLOADED = 3;
	public static final TpSvcUnavailReason SERVICE_UNAVAILABLE_OVERLOADED = new TpSvcUnavailReason(_SERVICE_UNAVAILABLE_OVERLOADED);
	public static final int _SERVICE_UNAVAILABLE_CLOSED = 4;
	public static final TpSvcUnavailReason SERVICE_UNAVAILABLE_CLOSED = new TpSvcUnavailReason(_SERVICE_UNAVAILABLE_CLOSED);
	public int value()
	{
		return value;
	}
	public static TpSvcUnavailReason from_int(int value)
	{
		switch (value) {
			case _SERVICE_UNAVAILABLE_UNDEFINED: return SERVICE_UNAVAILABLE_UNDEFINED;
			case _SERVICE_UNAVAILABLE_LOCAL_FAILURE: return SERVICE_UNAVAILABLE_LOCAL_FAILURE;
			case _SERVICE_UNAVAILABLE_GATEWAY_FAILURE: return SERVICE_UNAVAILABLE_GATEWAY_FAILURE;
			case _SERVICE_UNAVAILABLE_OVERLOADED: return SERVICE_UNAVAILABLE_OVERLOADED;
			case _SERVICE_UNAVAILABLE_CLOSED: return SERVICE_UNAVAILABLE_CLOSED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSvcUnavailReason(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
