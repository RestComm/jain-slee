package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationResponseIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationResponseIndicator
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_NO_DELAY = 0;
	public static final TpLocationResponseIndicator P_M_NO_DELAY = new TpLocationResponseIndicator(_P_M_NO_DELAY);
	public static final int _P_M_LOW_DELAY = 1;
	public static final TpLocationResponseIndicator P_M_LOW_DELAY = new TpLocationResponseIndicator(_P_M_LOW_DELAY);
	public static final int _P_M_DELAY_TOLERANT = 2;
	public static final TpLocationResponseIndicator P_M_DELAY_TOLERANT = new TpLocationResponseIndicator(_P_M_DELAY_TOLERANT);
	public static final int _P_M_USE_TIMER_VALUE = 3;
	public static final TpLocationResponseIndicator P_M_USE_TIMER_VALUE = new TpLocationResponseIndicator(_P_M_USE_TIMER_VALUE);
	public int value()
	{
		return value;
	}
	public static TpLocationResponseIndicator from_int(int value)
	{
		switch (value) {
			case _P_M_NO_DELAY: return P_M_NO_DELAY;
			case _P_M_LOW_DELAY: return P_M_LOW_DELAY;
			case _P_M_DELAY_TOLERANT: return P_M_DELAY_TOLERANT;
			case _P_M_USE_TIMER_VALUE: return P_M_USE_TIMER_VALUE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLocationResponseIndicator(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
