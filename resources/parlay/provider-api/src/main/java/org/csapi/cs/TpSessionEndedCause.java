package org.csapi.cs;
/**
 *	Generated from IDL definition of enum "TpSessionEndedCause"
 *	@author JacORB IDL compiler 
 */

public final class TpSessionEndedCause
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CHS_CAUSE_UNDEFINED = 0;
	public static final TpSessionEndedCause P_CHS_CAUSE_UNDEFINED = new TpSessionEndedCause(_P_CHS_CAUSE_UNDEFINED);
	public static final int _P_CHS_CAUSE_TIMER_EXPIRED = 1;
	public static final TpSessionEndedCause P_CHS_CAUSE_TIMER_EXPIRED = new TpSessionEndedCause(_P_CHS_CAUSE_TIMER_EXPIRED);
	public int value()
	{
		return value;
	}
	public static TpSessionEndedCause from_int(int value)
	{
		switch (value) {
			case _P_CHS_CAUSE_UNDEFINED: return P_CHS_CAUSE_UNDEFINED;
			case _P_CHS_CAUSE_TIMER_EXPIRED: return P_CHS_CAUSE_TIMER_EXPIRED;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSessionEndedCause(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
