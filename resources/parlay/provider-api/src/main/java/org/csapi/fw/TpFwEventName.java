package org.csapi.fw;
/**
 *	Generated from IDL definition of enum "TpFwEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_EVENT_FW_NAME_UNDEFINED = 0;
	public static final TpFwEventName P_EVENT_FW_NAME_UNDEFINED = new TpFwEventName(_P_EVENT_FW_NAME_UNDEFINED);
	public static final int _P_EVENT_FW_SERVICE_AVAILABLE = 1;
	public static final TpFwEventName P_EVENT_FW_SERVICE_AVAILABLE = new TpFwEventName(_P_EVENT_FW_SERVICE_AVAILABLE);
	public static final int _P_EVENT_FW_SERVICE_UNAVAILABLE = 2;
	public static final TpFwEventName P_EVENT_FW_SERVICE_UNAVAILABLE = new TpFwEventName(_P_EVENT_FW_SERVICE_UNAVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpFwEventName from_int(int value)
	{
		switch (value) {
			case _P_EVENT_FW_NAME_UNDEFINED: return P_EVENT_FW_NAME_UNDEFINED;
			case _P_EVENT_FW_SERVICE_AVAILABLE: return P_EVENT_FW_SERVICE_AVAILABLE;
			case _P_EVENT_FW_SERVICE_UNAVAILABLE: return P_EVENT_FW_SERVICE_UNAVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpFwEventName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
