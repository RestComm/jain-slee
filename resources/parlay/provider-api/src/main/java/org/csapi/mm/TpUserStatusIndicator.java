package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserStatusIndicator"
 *	@author JacORB IDL compiler 
 */

public final class TpUserStatusIndicator
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_US_REACHABLE = 0;
	public static final TpUserStatusIndicator P_US_REACHABLE = new TpUserStatusIndicator(_P_US_REACHABLE);
	public static final int _P_US_NOT_REACHABLE = 1;
	public static final TpUserStatusIndicator P_US_NOT_REACHABLE = new TpUserStatusIndicator(_P_US_NOT_REACHABLE);
	public static final int _P_US_BUSY = 2;
	public static final TpUserStatusIndicator P_US_BUSY = new TpUserStatusIndicator(_P_US_BUSY);
	public int value()
	{
		return value;
	}
	public static TpUserStatusIndicator from_int(int value)
	{
		switch (value) {
			case _P_US_REACHABLE: return P_US_REACHABLE;
			case _P_US_NOT_REACHABLE: return P_US_NOT_REACHABLE;
			case _P_US_BUSY: return P_US_BUSY;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpUserStatusIndicator(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
