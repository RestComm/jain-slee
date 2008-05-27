package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpAppAvailStatusReason"
 *	@author JacORB IDL compiler 
 */

public final class TpAppAvailStatusReason
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _APP_UNAVAILABLE_UNDEFINED = 0;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_UNDEFINED = new TpAppAvailStatusReason(_APP_UNAVAILABLE_UNDEFINED);
	public static final int _APP_UNAVAILABLE_LOCAL_FAILURE = 1;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_LOCAL_FAILURE = new TpAppAvailStatusReason(_APP_UNAVAILABLE_LOCAL_FAILURE);
	public static final int _APP_UNAVAILABLE_REMOTE_FAILURE = 2;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_REMOTE_FAILURE = new TpAppAvailStatusReason(_APP_UNAVAILABLE_REMOTE_FAILURE);
	public static final int _APP_UNAVAILABLE_OVERLOADED = 3;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_OVERLOADED = new TpAppAvailStatusReason(_APP_UNAVAILABLE_OVERLOADED);
	public static final int _APP_UNAVAILABLE_CLOSED = 4;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_CLOSED = new TpAppAvailStatusReason(_APP_UNAVAILABLE_CLOSED);
	public static final int _APP_UNAVAILABLE_NO_RESPONSE = 5;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_NO_RESPONSE = new TpAppAvailStatusReason(_APP_UNAVAILABLE_NO_RESPONSE);
	public static final int _APP_UNAVAILABLE_SW_UPGRADE = 6;
	public static final TpAppAvailStatusReason APP_UNAVAILABLE_SW_UPGRADE = new TpAppAvailStatusReason(_APP_UNAVAILABLE_SW_UPGRADE);
	public static final int _APP_AVAILABLE = 7;
	public static final TpAppAvailStatusReason APP_AVAILABLE = new TpAppAvailStatusReason(_APP_AVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpAppAvailStatusReason from_int(int value)
	{
		switch (value) {
			case _APP_UNAVAILABLE_UNDEFINED: return APP_UNAVAILABLE_UNDEFINED;
			case _APP_UNAVAILABLE_LOCAL_FAILURE: return APP_UNAVAILABLE_LOCAL_FAILURE;
			case _APP_UNAVAILABLE_REMOTE_FAILURE: return APP_UNAVAILABLE_REMOTE_FAILURE;
			case _APP_UNAVAILABLE_OVERLOADED: return APP_UNAVAILABLE_OVERLOADED;
			case _APP_UNAVAILABLE_CLOSED: return APP_UNAVAILABLE_CLOSED;
			case _APP_UNAVAILABLE_NO_RESPONSE: return APP_UNAVAILABLE_NO_RESPONSE;
			case _APP_UNAVAILABLE_SW_UPGRADE: return APP_UNAVAILABLE_SW_UPGRADE;
			case _APP_AVAILABLE: return APP_AVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAppAvailStatusReason(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
