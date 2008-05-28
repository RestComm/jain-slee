package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallEventType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_EVENT_UNDEFINED = 0;
	public static final TpCallEventType P_CALL_EVENT_UNDEFINED = new TpCallEventType(_P_CALL_EVENT_UNDEFINED);
	public static final int _P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT = 1;
	public static final TpCallEventType P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT = new TpCallEventType(_P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT);
	public static final int _P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED = 2;
	public static final TpCallEventType P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED = new TpCallEventType(_P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED);
	public static final int _P_CALL_EVENT_ADDRESS_COLLECTED = 3;
	public static final TpCallEventType P_CALL_EVENT_ADDRESS_COLLECTED = new TpCallEventType(_P_CALL_EVENT_ADDRESS_COLLECTED);
	public static final int _P_CALL_EVENT_ADDRESS_ANALYSED = 4;
	public static final TpCallEventType P_CALL_EVENT_ADDRESS_ANALYSED = new TpCallEventType(_P_CALL_EVENT_ADDRESS_ANALYSED);
	public static final int _P_CALL_EVENT_ORIGINATING_SERVICE_CODE = 5;
	public static final TpCallEventType P_CALL_EVENT_ORIGINATING_SERVICE_CODE = new TpCallEventType(_P_CALL_EVENT_ORIGINATING_SERVICE_CODE);
	public static final int _P_CALL_EVENT_ORIGINATING_RELEASE = 6;
	public static final TpCallEventType P_CALL_EVENT_ORIGINATING_RELEASE = new TpCallEventType(_P_CALL_EVENT_ORIGINATING_RELEASE);
	public static final int _P_CALL_EVENT_TERMINATING_CALL_ATTEMPT = 7;
	public static final TpCallEventType P_CALL_EVENT_TERMINATING_CALL_ATTEMPT = new TpCallEventType(_P_CALL_EVENT_TERMINATING_CALL_ATTEMPT);
	public static final int _P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED = 8;
	public static final TpCallEventType P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED = new TpCallEventType(_P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED);
	public static final int _P_CALL_EVENT_ALERTING = 9;
	public static final TpCallEventType P_CALL_EVENT_ALERTING = new TpCallEventType(_P_CALL_EVENT_ALERTING);
	public static final int _P_CALL_EVENT_ANSWER = 10;
	public static final TpCallEventType P_CALL_EVENT_ANSWER = new TpCallEventType(_P_CALL_EVENT_ANSWER);
	public static final int _P_CALL_EVENT_TERMINATING_RELEASE = 11;
	public static final TpCallEventType P_CALL_EVENT_TERMINATING_RELEASE = new TpCallEventType(_P_CALL_EVENT_TERMINATING_RELEASE);
	public static final int _P_CALL_EVENT_REDIRECTED = 12;
	public static final TpCallEventType P_CALL_EVENT_REDIRECTED = new TpCallEventType(_P_CALL_EVENT_REDIRECTED);
	public static final int _P_CALL_EVENT_TERMINATING_SERVICE_CODE = 13;
	public static final TpCallEventType P_CALL_EVENT_TERMINATING_SERVICE_CODE = new TpCallEventType(_P_CALL_EVENT_TERMINATING_SERVICE_CODE);
	public static final int _P_CALL_EVENT_QUEUED = 14;
	public static final TpCallEventType P_CALL_EVENT_QUEUED = new TpCallEventType(_P_CALL_EVENT_QUEUED);
	public int value()
	{
		return value;
	}
	public static TpCallEventType from_int(int value)
	{
		switch (value) {
			case _P_CALL_EVENT_UNDEFINED: return P_CALL_EVENT_UNDEFINED;
			case _P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT: return P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT;
			case _P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED: return P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED;
			case _P_CALL_EVENT_ADDRESS_COLLECTED: return P_CALL_EVENT_ADDRESS_COLLECTED;
			case _P_CALL_EVENT_ADDRESS_ANALYSED: return P_CALL_EVENT_ADDRESS_ANALYSED;
			case _P_CALL_EVENT_ORIGINATING_SERVICE_CODE: return P_CALL_EVENT_ORIGINATING_SERVICE_CODE;
			case _P_CALL_EVENT_ORIGINATING_RELEASE: return P_CALL_EVENT_ORIGINATING_RELEASE;
			case _P_CALL_EVENT_TERMINATING_CALL_ATTEMPT: return P_CALL_EVENT_TERMINATING_CALL_ATTEMPT;
			case _P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED: return P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED;
			case _P_CALL_EVENT_ALERTING: return P_CALL_EVENT_ALERTING;
			case _P_CALL_EVENT_ANSWER: return P_CALL_EVENT_ANSWER;
			case _P_CALL_EVENT_TERMINATING_RELEASE: return P_CALL_EVENT_TERMINATING_RELEASE;
			case _P_CALL_EVENT_REDIRECTED: return P_CALL_EVENT_REDIRECTED;
			case _P_CALL_EVENT_TERMINATING_SERVICE_CODE: return P_CALL_EVENT_TERMINATING_SERVICE_CODE;
			case _P_CALL_EVENT_QUEUED: return P_CALL_EVENT_QUEUED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallEventType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
