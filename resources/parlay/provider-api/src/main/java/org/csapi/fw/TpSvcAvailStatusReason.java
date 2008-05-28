package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpSvcAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpSvcAvailStatusReason
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SVC_UNAVAILABLE_UNDEFINED = 0;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_UNDEFINED = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_UNDEFINED);
	public static final int _SVC_UNAVAILABLE_LOCAL_FAILURE = 1;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_LOCAL_FAILURE = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_LOCAL_FAILURE);
	public static final int _SVC_UNAVAILABLE_GATEWAY_FAILURE = 2;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_GATEWAY_FAILURE = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_GATEWAY_FAILURE);
	public static final int _SVC_UNAVAILABLE_OVERLOADED = 3;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_OVERLOADED = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_OVERLOADED);
	public static final int _SVC_UNAVAILABLE_CLOSED = 4;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_CLOSED = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_CLOSED);
	public static final int _SVC_UNAVAILABLE_NO_RESPONSE = 5;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_NO_RESPONSE = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_NO_RESPONSE);
	public static final int _SVC_UNAVAILABLE_SW_UPGRADE = 6;
	public static final TpSvcAvailStatusReason SVC_UNAVAILABLE_SW_UPGRADE = new TpSvcAvailStatusReason(_SVC_UNAVAILABLE_SW_UPGRADE);
	public static final int _SVC_AVAILABLE = 7;
	public static final TpSvcAvailStatusReason SVC_AVAILABLE = new TpSvcAvailStatusReason(_SVC_AVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpSvcAvailStatusReason from_int(int value)
	{
		switch (value) {
			case _SVC_UNAVAILABLE_UNDEFINED: return SVC_UNAVAILABLE_UNDEFINED;
			case _SVC_UNAVAILABLE_LOCAL_FAILURE: return SVC_UNAVAILABLE_LOCAL_FAILURE;
			case _SVC_UNAVAILABLE_GATEWAY_FAILURE: return SVC_UNAVAILABLE_GATEWAY_FAILURE;
			case _SVC_UNAVAILABLE_OVERLOADED: return SVC_UNAVAILABLE_OVERLOADED;
			case _SVC_UNAVAILABLE_CLOSED: return SVC_UNAVAILABLE_CLOSED;
			case _SVC_UNAVAILABLE_NO_RESPONSE: return SVC_UNAVAILABLE_NO_RESPONSE;
			case _SVC_UNAVAILABLE_SW_UPGRADE: return SVC_UNAVAILABLE_SW_UPGRADE;
			case _SVC_AVAILABLE: return SVC_AVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSvcAvailStatusReason(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
