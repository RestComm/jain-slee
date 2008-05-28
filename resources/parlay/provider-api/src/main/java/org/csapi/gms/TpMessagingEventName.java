package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagingEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_EVENT_GMS_NAME_UNDEFINED = 0;
	public static final TpMessagingEventName P_EVENT_GMS_NAME_UNDEFINED = new TpMessagingEventName(_P_EVENT_GMS_NAME_UNDEFINED);
	public static final int _P_EVENT_GMS_NEW_MESSAGE_ARRIVED = 1;
	public static final TpMessagingEventName P_EVENT_GMS_NEW_MESSAGE_ARRIVED = new TpMessagingEventName(_P_EVENT_GMS_NEW_MESSAGE_ARRIVED);
	public int value()
	{
		return value;
	}
	public static TpMessagingEventName from_int(int value)
	{
		switch (value) {
			case _P_EVENT_GMS_NAME_UNDEFINED: return P_EVENT_GMS_NAME_UNDEFINED;
			case _P_EVENT_GMS_NEW_MESSAGE_ARRIVED: return P_EVENT_GMS_NEW_MESSAGE_ARRIVED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMessagingEventName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
