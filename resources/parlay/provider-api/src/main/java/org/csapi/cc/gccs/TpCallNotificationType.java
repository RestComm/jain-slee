package org.csapi.cc.gccs;
/**
 *	Generated from IDL definition of enum "TpCallNotificationType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ORIGINATING = 0;
	public static final TpCallNotificationType P_ORIGINATING = new TpCallNotificationType(_P_ORIGINATING);
	public static final int _P_TERMINATING = 1;
	public static final TpCallNotificationType P_TERMINATING = new TpCallNotificationType(_P_TERMINATING);
	public int value()
	{
		return value;
	}
	public static TpCallNotificationType from_int(int value)
	{
		switch (value) {
			case _P_ORIGINATING: return P_ORIGINATING;
			case _P_TERMINATING: return P_TERMINATING;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallNotificationType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
