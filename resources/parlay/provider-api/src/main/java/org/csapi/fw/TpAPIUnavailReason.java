package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAPIUnavailReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAPIUnavailReason
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _API_UNAVAILABLE_UNDEFINED = 0;
	public static final TpAPIUnavailReason API_UNAVAILABLE_UNDEFINED = new TpAPIUnavailReason(_API_UNAVAILABLE_UNDEFINED);
	public static final int _API_UNAVAILABLE_LOCAL_FAILURE = 1;
	public static final TpAPIUnavailReason API_UNAVAILABLE_LOCAL_FAILURE = new TpAPIUnavailReason(_API_UNAVAILABLE_LOCAL_FAILURE);
	public static final int _API_UNAVAILABLE_GATEWAY_FAILURE = 2;
	public static final TpAPIUnavailReason API_UNAVAILABLE_GATEWAY_FAILURE = new TpAPIUnavailReason(_API_UNAVAILABLE_GATEWAY_FAILURE);
	public static final int _API_UNAVAILABLE_OVERLOADED = 3;
	public static final TpAPIUnavailReason API_UNAVAILABLE_OVERLOADED = new TpAPIUnavailReason(_API_UNAVAILABLE_OVERLOADED);
	public static final int _API_UNAVAILABLE_CLOSED = 4;
	public static final TpAPIUnavailReason API_UNAVAILABLE_CLOSED = new TpAPIUnavailReason(_API_UNAVAILABLE_CLOSED);
	public static final int _API_UNAVAILABLE_PROTOCOL_FAILURE = 5;
	public static final TpAPIUnavailReason API_UNAVAILABLE_PROTOCOL_FAILURE = new TpAPIUnavailReason(_API_UNAVAILABLE_PROTOCOL_FAILURE);
	public int value()
	{
		return value;
	}
	public static TpAPIUnavailReason from_int(int value)
	{
		switch (value) {
			case _API_UNAVAILABLE_UNDEFINED: return API_UNAVAILABLE_UNDEFINED;
			case _API_UNAVAILABLE_LOCAL_FAILURE: return API_UNAVAILABLE_LOCAL_FAILURE;
			case _API_UNAVAILABLE_GATEWAY_FAILURE: return API_UNAVAILABLE_GATEWAY_FAILURE;
			case _API_UNAVAILABLE_OVERLOADED: return API_UNAVAILABLE_OVERLOADED;
			case _API_UNAVAILABLE_CLOSED: return API_UNAVAILABLE_CLOSED;
			case _API_UNAVAILABLE_PROTOCOL_FAILURE: return API_UNAVAILABLE_PROTOCOL_FAILURE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAPIUnavailReason(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
