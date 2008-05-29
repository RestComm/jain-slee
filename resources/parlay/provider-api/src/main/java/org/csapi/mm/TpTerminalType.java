package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpTerminalType"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_FIXED = 0;
	public static final TpTerminalType P_M_FIXED = new TpTerminalType(_P_M_FIXED);
	public static final int _P_M_MOBILE = 1;
	public static final TpTerminalType P_M_MOBILE = new TpTerminalType(_P_M_MOBILE);
	public static final int _P_M_IP = 2;
	public static final TpTerminalType P_M_IP = new TpTerminalType(_P_M_IP);
	public int value()
	{
		return value;
	}
	public static TpTerminalType from_int(int value)
	{
		switch (value) {
			case _P_M_FIXED: return P_M_FIXED;
			case _P_M_MOBILE: return P_M_MOBILE;
			case _P_M_IP: return P_M_IP;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpTerminalType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
