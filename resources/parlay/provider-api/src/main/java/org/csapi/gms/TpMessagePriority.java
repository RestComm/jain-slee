package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpMessagePriority"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagePriority
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED = 0;
	public static final TpMessagePriority P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED = new TpMessagePriority(_P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED);
	public static final int _P_MESSAGING_MESSAGE_PRIORITY_HIGH = 1;
	public static final TpMessagePriority P_MESSAGING_MESSAGE_PRIORITY_HIGH = new TpMessagePriority(_P_MESSAGING_MESSAGE_PRIORITY_HIGH);
	public static final int _P_MESSAGING_MESSAGE_PRIORITY_LOW = 2;
	public static final TpMessagePriority P_MESSAGING_MESSAGE_PRIORITY_LOW = new TpMessagePriority(_P_MESSAGING_MESSAGE_PRIORITY_LOW);
	public int value()
	{
		return value;
	}
	public static TpMessagePriority from_int(int value)
	{
		switch (value) {
			case _P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED: return P_MESSAGING_MESSAGE_PRIORITY_UNDEFINED;
			case _P_MESSAGING_MESSAGE_PRIORITY_HIGH: return P_MESSAGING_MESSAGE_PRIORITY_HIGH;
			case _P_MESSAGING_MESSAGE_PRIORITY_LOW: return P_MESSAGING_MESSAGE_PRIORITY_LOW;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpMessagePriority(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
