package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpReleaseCause"
 *	@author JacORB IDL compiler 
 */

public final class TpReleaseCause
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_UNDEFINED = 0;
	public static final TpReleaseCause P_UNDEFINED = new TpReleaseCause(_P_UNDEFINED);
	public static final int _P_USER_NOT_AVAILABLE = 1;
	public static final TpReleaseCause P_USER_NOT_AVAILABLE = new TpReleaseCause(_P_USER_NOT_AVAILABLE);
	public static final int _P_BUSY = 2;
	public static final TpReleaseCause P_BUSY = new TpReleaseCause(_P_BUSY);
	public static final int _P_NO_ANSWER = 3;
	public static final TpReleaseCause P_NO_ANSWER = new TpReleaseCause(_P_NO_ANSWER);
	public static final int _P_NOT_REACHABLE = 4;
	public static final TpReleaseCause P_NOT_REACHABLE = new TpReleaseCause(_P_NOT_REACHABLE);
	public static final int _P_ROUTING_FAILURE = 5;
	public static final TpReleaseCause P_ROUTING_FAILURE = new TpReleaseCause(_P_ROUTING_FAILURE);
	public static final int _P_PREMATURE_DISCONNECT = 6;
	public static final TpReleaseCause P_PREMATURE_DISCONNECT = new TpReleaseCause(_P_PREMATURE_DISCONNECT);
	public static final int _P_DISCONNECTED = 7;
	public static final TpReleaseCause P_DISCONNECTED = new TpReleaseCause(_P_DISCONNECTED);
	public static final int _P_CALL_RESTRICTED = 8;
	public static final TpReleaseCause P_CALL_RESTRICTED = new TpReleaseCause(_P_CALL_RESTRICTED);
	public static final int _P_UNAVAILABLE_RESOURCE = 9;
	public static final TpReleaseCause P_UNAVAILABLE_RESOURCE = new TpReleaseCause(_P_UNAVAILABLE_RESOURCE);
	public static final int _P_GENERAL_FAILURE = 10;
	public static final TpReleaseCause P_GENERAL_FAILURE = new TpReleaseCause(_P_GENERAL_FAILURE);
	public static final int _P_TIMER_EXPIRY = 11;
	public static final TpReleaseCause P_TIMER_EXPIRY = new TpReleaseCause(_P_TIMER_EXPIRY);
	public static final int _P_UNSUPPORTED_MEDIA = 12;
	public static final TpReleaseCause P_UNSUPPORTED_MEDIA = new TpReleaseCause(_P_UNSUPPORTED_MEDIA);
	public int value()
	{
		return value;
	}
	public static TpReleaseCause from_int(int value)
	{
		switch (value) {
			case _P_UNDEFINED: return P_UNDEFINED;
			case _P_USER_NOT_AVAILABLE: return P_USER_NOT_AVAILABLE;
			case _P_BUSY: return P_BUSY;
			case _P_NO_ANSWER: return P_NO_ANSWER;
			case _P_NOT_REACHABLE: return P_NOT_REACHABLE;
			case _P_ROUTING_FAILURE: return P_ROUTING_FAILURE;
			case _P_PREMATURE_DISCONNECT: return P_PREMATURE_DISCONNECT;
			case _P_DISCONNECTED: return P_DISCONNECTED;
			case _P_CALL_RESTRICTED: return P_CALL_RESTRICTED;
			case _P_UNAVAILABLE_RESOURCE: return P_UNAVAILABLE_RESOURCE;
			case _P_GENERAL_FAILURE: return P_GENERAL_FAILURE;
			case _P_TIMER_EXPIRY: return P_TIMER_EXPIRY;
			case _P_UNSUPPORTED_MEDIA: return P_UNSUPPORTED_MEDIA;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpReleaseCause(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
