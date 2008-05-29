package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationType"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_CURRENT = 0;
	public static final TpLocationType P_M_CURRENT = new TpLocationType(_P_M_CURRENT);
	public static final int _P_M_CURRENT_OR_LAST_KNOWN = 1;
	public static final TpLocationType P_M_CURRENT_OR_LAST_KNOWN = new TpLocationType(_P_M_CURRENT_OR_LAST_KNOWN);
	public static final int _P_M_INITIAL = 2;
	public static final TpLocationType P_M_INITIAL = new TpLocationType(_P_M_INITIAL);
	public int value()
	{
		return value;
	}
	public static TpLocationType from_int(int value)
	{
		switch (value) {
			case _P_M_CURRENT: return P_M_CURRENT;
			case _P_M_CURRENT_OR_LAST_KNOWN: return P_M_CURRENT_OR_LAST_KNOWN;
			case _P_M_INITIAL: return P_M_INITIAL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLocationType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
