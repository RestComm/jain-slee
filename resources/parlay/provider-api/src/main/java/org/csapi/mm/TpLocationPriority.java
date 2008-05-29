package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationPriority"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationPriority
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_NORMAL = 0;
	public static final TpLocationPriority P_M_NORMAL = new TpLocationPriority(_P_M_NORMAL);
	public static final int _P_M_HIGH = 1;
	public static final TpLocationPriority P_M_HIGH = new TpLocationPriority(_P_M_HIGH);
	public int value()
	{
		return value;
	}
	public static TpLocationPriority from_int(int value)
	{
		switch (value) {
			case _P_M_NORMAL: return P_M_NORMAL;
			case _P_M_HIGH: return P_M_HIGH;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLocationPriority(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
